/*
 * Copyright (c) 2008, Ivan Appel <ivan.appel@gmail.com>
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer. 
 * - Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. 
 * 
 * Neither the name of Ivan Appel nor the names of any other jThinker
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package oss.jthinker.views;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import oss.jthinker.datamodel.DiagramData;
import oss.jthinker.datamodel.DiagramType;
import oss.jthinker.util.TriggerEvent;
import static oss.jthinker.swingutils.ThinkerFileChooser.*;

/**
 * Tabbed pane that contains several {@link DiagramPane}s.
 * 
 * @author iappel
 */
public abstract class DiagramDeck extends JTabbedPane {
    private static final Logger logger = Logger.getAnonymousLogger();

    /**
     * Creates a new instance of DiagramDeck.
     */
    public DiagramDeck() {
        this.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                contentChanged(getCurrentDiagram());
            }
        });
    }
    
    /**
     * Adds a new empty {@link DiagramPane}.
     * 
     * @param desc description of the new diagram
     */
    public void addLinkPane(DiagramType type) {
        DiagramPane linkPane = new DiagramPane(type);
        String title = type.getTitle();
        doAddPane(linkPane, title);
    }

    /**
     * Loads diagram from file and adds a new {@link DiagramPane}.
     * 
     * @param file diagram's file
     * @throws SAXException on parsing errors
     * @throws IOException on I/O errors of loading a file
     */
    public void addLinkPane(File file)
    throws IOException, SAXException, ParserConfigurationException {
        DiagramData data = new DiagramData(file);
        doAddPane(new DiagramPane(data), file.toString());
    }
    
    private void doAddPane(DiagramPane pane, String title) {
        JScrollPane scrollPane = new JScrollPane(pane);
        addTab(title, scrollPane);
        setSelectedComponent(scrollPane);
        int selectedIndex = getSelectedIndex();
        setTabComponentAt(selectedIndex, new TabHeader(pane, this));
    }
    
    /**
     * Returns diagram that is currently selected.
     * 
     * @return diagram that is currently selected.
     */
    protected DiagramPane getCurrentDiagram() {
        return getDiagram(getSelectedIndex());
    }

    /**
     * Returns list of all diagrams.
     * 
     * @return list of all diagrams.
     */
    protected List<DiagramPane> getAllDiagrams() {
        ArrayList<DiagramPane> result = new ArrayList<DiagramPane>();
        for (int i=0;i<getTabCount();i++) {
            DiagramPane pane = getDiagram(i);
            if (pane != null) result.add(pane);
        }
        return result;
    }

    /**
     * Returns diagram at the specified index.
     * 
     * @param i index of the diagram
     * @return diagram at the given index
     */
    public DiagramPane getDiagram(int i) {
        if (i == -1) {
            return null;
        }
        Component component = getComponentAt(i);
        if (!(component instanceof JScrollPane)) return null;
        JScrollPane scrollPane = (JScrollPane)component;
        JViewport viewport = (JViewport)scrollPane.getComponent(0);
        component = viewport.getComponent(0);
        if (component instanceof DiagramPane) {
            return (DiagramPane)component;
        }
        return null;
    }

    /**
     * Returns index of the specified diagram.
     * 
     * @param pane diagram to seek
     * @return index of the specified diagram or -1 if diagram is
     * not contained by given pane.
     */
    public int getDiagramIndex(DiagramPane pane) {
        for (int i=0;i<getTabCount();i++) {
            if (pane == getDiagram(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Closes all diagrams.
     * 
     * @return true if all diagrams were closed successfully
     */
    public boolean closeAll() {
        for (DiagramPane pane : getAllDiagrams()) {
            if (pane.isSaved()) {
                continue;
            }
            if (!pane.askedSave()) {
                return false;
            }
        }
        return true;
    }


    /**
     * Loads a diagram from file.
     */
    public void loadNew() {
        File file = chooseLoad(JTHINKER_FILES);
        if (file == null) {
            return;
        }
        try {
            addLinkPane(file);
        } catch (Throwable t) {
            logger.log(Level.SEVERE, "Unable to open", t);
        }
    }

    /**
     * Method that is called each time the selection is changed.
     * 
     * @param pane newly selected diagram pane.
     */
    protected abstract void contentChanged(DiagramPane pane);

    public void addDefaultPane() {
        add("jThinker", new DefaultPane(this));
    }
}
