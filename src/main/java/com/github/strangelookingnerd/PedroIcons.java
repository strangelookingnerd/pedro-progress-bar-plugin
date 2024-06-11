/*
 * The MIT License
 *
 * Copyright (c) 2024 strangelookingnerd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.strangelookingnerd;

import com.intellij.ui.scale.JBUIScale;

import javax.swing.ImageIcon;

public final class PedroIcons {

    private static final ImageIcon EMPTY = new ImageIcon(/* used if we can't scale the image */);
    private static final ImageIcon PIXEL_ART_PEDRO_16 = new ImageIcon(PedroIcons.class.getResource("/pedro_16.gif"),
            "Pedro 16x16");
    private static final ImageIcon PIXEL_ART_PEDRO_17 = new ImageIcon(PedroIcons.class.getResource("/pedro_17.gif"),
            "Pedro 17x17");
    private static final ImageIcon PIXEL_ART_PEDRO_20 = new ImageIcon(PedroIcons.class.getResource("/pedro_20.gif"),
            "Pedro 20x20");
    private static final ImageIcon PIXEL_ART_PEDRO_24 = new ImageIcon(PedroIcons.class.getResource("/pedro_24.gif"),
            "Pedro 24x24");
    private static final ImageIcon PIXEL_ART_PEDRO_28 = new ImageIcon(PedroIcons.class.getResource("/pedro_28.gif"),
            "Pedro 28x28");
    private static final ImageIcon PIXEL_ART_PEDRO_32 = new ImageIcon(PedroIcons.class.getResource("/pedro_32.gif"),
            "Pedro 32x32");

    private PedroIcons() {
        // hidden
    }

    /**
     * Scales the image icon based on the available zoom factors.
     * @return the scaled image icon
     */
    public static ImageIcon getScaledIcon() {
        return switch (JBUIScale.scale(100)) {
            case 100 -> PIXEL_ART_PEDRO_16;
            case 110 -> PIXEL_ART_PEDRO_17;
            case 125 -> PIXEL_ART_PEDRO_20;
            case 150 -> PIXEL_ART_PEDRO_24;
            case 175 -> PIXEL_ART_PEDRO_28;
            case 200 -> PIXEL_ART_PEDRO_32;
            default -> EMPTY;
        };
    }

}

