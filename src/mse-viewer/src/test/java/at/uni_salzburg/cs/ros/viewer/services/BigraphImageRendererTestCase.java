/*
 * This code is part of the BigActor project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.ros.viewer.services;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BigraphImageRendererTestCase
{
    private static final String BGM_TEST_01 = "l0_Location.uav0[x] | l1_Location.uav1[x]";

    private static final String BGM_TEST_02 =
        "airfield_Location.(u0_UAV[wifi]\n" +
            "                 | u1_UAV[wifi]\n" +
            "                 | cs0_ControlStation[wifi])\n" +
            "| harbour_Location.(vessel0_Vessel[x,ais].(drifter0_Drifter[ais]\n" +
            "                                         | drifter1_Drifter[ais]))\n" +
            "|" +
            "| searchArea_Location";

    private static final String[][] bigraphTests =
    {
        {BGM_TEST_01, "BigraphImageRendererTestImage001.png"},
        {BGM_TEST_02, "BigraphImageRendererTestImage002.png"},
    };

    @Test
    public void shouldRenderCorectBigraphsCorrectly() throws IOException
    {
        for (String[] bigraph : bigraphTests)
        {
            BigraphImageRenderer renderer = new BigraphImageRendererImpl();
            byte[] actuals = renderer.convertBgmToPng(bigraph[0]);
            assertNotNull(actuals);

            InputStream is = getClass().getResourceAsStream(bigraph[1]);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtils.copyLarge(is, os);
            is.close();
            os.close();
            byte[] expecteds = os.toByteArray();
            
            assertArrayEquals("Rendered bigraph image differs from expected image " + bigraph[1], expecteds, actuals);
        }
    }

}
