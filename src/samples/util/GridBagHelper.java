/*
 * Scope: a generic MVC framework.
 * Copyright (c) 2000-2002, The Scope team
 * All rights reserved.
 *
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * Neither the name "Scope" nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * $Id: pretty.settings,v 1.4 2002/09/19 18:10:27 ludovicc Exp $
 */
package samples.util;

import java.awt.*;

/**
 * Helper for creating GridBagConstraints with convenience short methods. Fill
 * and anchor are specified with single string containing letters 'n' (north),
 * 'e' (east), 's' (south), 'w' (west) specifying in which direction from center
 * span the component
 *
 * @author daniel.michalik
 * @version $Revision: 1.4 $
 * @created 05 September 2002
 */
public class GridBagHelper implements java.io.Serializable {
    /**
     * TODO: describe of the Field
     */
    public int x;
    /**
     * TODO: describe of the Field
     */
    public int y;

    /**
     * Constructor for the GridBagHelper object
     */
    public GridBagHelper() { }

    /**
     * TODO: document the method
     *
     * @param x TODO: Describe the Parameter
     * @param y TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints xy(int x, int y) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        this.x = x;
        this.y = y;
        return c;
    }

    /**
     * TODO: document the method
     *
     * @param x TODO: Describe the Parameter
     * @param y TODO: Describe the Parameter
     * @param fillanchor TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints xy(int x, int y, String fillanchor) {
        this.x = x;
        this.y = y;
        return setFillAnchor(xy(x, y), fillanchor);
    }

    /**
     * TODO: document the method
     *
     * @param x TODO: Describe the Parameter
     * @param fillanchor TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints x(int x, String fillanchor) {
        return xy(x, y, fillanchor);
    }

    /**
     * TODO: document the method
     *
     * @param y TODO: Describe the Parameter
     * @param fillanchor TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints y(int y, String fillanchor) {
        return xy(x, y, fillanchor);
    }

    /**
     * TODO: document the method
     *
     * @param x TODO: Describe the Parameter
     * @param w TODO: Describe the Parameter
     * @param h TODO: Describe the Parameter
     * @param fillanchor TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints xwh(int x, int w, int h, String fillanchor) {
        return xywh(x, y, w, h, fillanchor);
    }

    /**
     * TODO: document the method
     *
     * @param y TODO: Describe the Parameter
     * @param w TODO: Describe the Parameter
     * @param h TODO: Describe the Parameter
     * @param fillanchor TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints ywh(int y, int w, int h, String fillanchor) {
        return xywh(x, y, w, h, fillanchor);
    }

    /**
     * TODO: document the method
     *
     * @param x TODO: Describe the Parameter
     * @param y TODO: Describe the Parameter
     * @param w TODO: Describe the Parameter
     * @param h TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints xywh(int x, int y, int w, int h) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.gridwidth = w;
        c.gridheight = h;
        this.x = x;
        this.y = y;
        return c;
    }

    /**
     * TODO: document the method
     *
     * @param x TODO: Describe the Parameter
     * @param y TODO: Describe the Parameter
     * @param w TODO: Describe the Parameter
     * @param h TODO: Describe the Parameter
     * @param fillanchor TODO: Describe the Parameter
     * @return TODO: Describe the Return Value
     */
    public GridBagConstraints xywh(int x, int y, int w, int h, String fillanchor) {
        this.x = x;
        this.y = y;
        return setFillAnchor(xywh(x, y, w, h), fillanchor);
    }

    /**
     * Adds an element to the X2 attribute of the GridBagHelper object
     */
    public void addX2() {
        x += 2;
    }

    /**
     * Adds an element to the X1 attribute of the GridBagHelper object
     */
    public void addX1() {
        x += 1;
    }

    /**
     * Adds an element to the Y2 attribute of the GridBagHelper object
     */
    public void addY2() {
        y += 2;
    }

    /**
     * Adds an element to the Y1 attribute of the GridBagHelper object
     */
    public void addY1() {
        y += 1;
    }

    private GridBagConstraints setFillAnchor(GridBagConstraints c, String a) {
        String s = a.toLowerCase();
        boolean b[] = {false, false, false, false};
        b[0] = (s.indexOf('n') > -1);
        b[1] = (s.indexOf('e') > -1);
        b[2] = (s.indexOf('s') > -1);
        b[3] = (s.indexOf('w') > -1);
        if (b[0] && b[1] && b[2] && b[3]) {
            c.fill = GridBagConstraints.BOTH;
            return c;
        } else if (b[0] && b[2]) {
            c.fill = GridBagConstraints.VERTICAL;
            b[0] = false;
            b[2] = false;
        } else if (b[1] && b[3]) {
            c.fill = GridBagConstraints.HORIZONTAL;
            b[1] = false;
            b[3] = false;
        }
        if (b[0] && b[1]) {
            c.anchor = GridBagConstraints.NORTHEAST;
        } else if (b[1] && b[2]) {
            c.anchor = GridBagConstraints.SOUTHEAST;
        } else if (b[2] && b[3]) {
            c.anchor = GridBagConstraints.SOUTHWEST;
        } else if (b[3] && b[0]) {
            c.anchor = GridBagConstraints.NORTHWEST;
        } else if (b[0]) {
            c.anchor = GridBagConstraints.NORTH;
        } else if (b[1]) {
            c.anchor = GridBagConstraints.EAST;
        } else if (b[2]) {
            c.anchor = GridBagConstraints.SOUTH;
        } else if (b[3]) {
            c.anchor = GridBagConstraints.WEST;
        }

        return c;
    }

}
