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
package oss.jthinker.datamodel;

import java.awt.Point;

/**
 * List of the valid nodes in thinking processes diagrams
 * 
 * @author iappel
 */
public enum NodeType {
    STATEMENT(BorderType.ROUND_RECT, true),
    TASK(BorderType.SHARP_RECT, true),
    OBSTACLE(BorderType.HEXAGON, true),
    ELLIPSE(BorderType.ELLIPSE, false);

    private final BorderType _borderType;
    private final boolean _editable;
    private final JNodeData _prototype;

    private NodeType(BorderType border, boolean editable) {
        _borderType = border;
        _editable = editable;
        _prototype = new JNodeData(border, editable, "", null);
    }

    public BorderType getBorderType() {
        return _borderType;
    }

    public boolean isEditable() {
        return _editable;
    }

    public JNodeData getPrototype() {
        return _prototype;
    }

    public JNodeData cloneNodeData(String content, Point p) {
        return _prototype.clone(content, p);
    }

    public static JNodeData closeEllipseNodeData(Point p) {
        return ELLIPSE.cloneNodeData("", p);
    }
}
