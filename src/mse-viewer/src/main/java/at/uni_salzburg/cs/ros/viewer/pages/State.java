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
package at.uni_salzburg.cs.ros.viewer.pages;

import at.uni_salzburg.cs.ros.viewer.services.ros.JsonConverter;
import at.uni_salzburg.cs.ros.viewer.services.ros.RosNodeStarter;

import big_actor_msgs.MissionStateEstimate;
import big_actor_msgs.StructureStateEstimate;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONCollection;
import org.apache.tapestry5.services.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * State page for map updating.
 */
public class State
{
//    private final Logger LOG = LoggerFactory.getLogger(Mse.class);

    @Inject
    private RosNodeStarter rosNodeStarter;

    @Inject
    private JsonConverter mseConverter;

    /**
     * @return the MSE JSON stream response.
     */
    public StreamResponse onActivate()
    {
        return onActivate(null);
    }

    /**
     * @param what the subset of the MSE to be emitted.
     * @return the MSE JSON stream response.
     */
    public StreamResponse onActivate(final String what)
    {
        return new MseStreamResponse(mseConverter, rosNodeStarter.getMseListener().getMessage(),
            rosNodeStarter.getSseListener().getMessage(),
            what);
    }

    /**
     * MseStreamResponse
     */
    private static class MseStreamResponse implements StreamResponse
    {
        private JsonConverter mseConverter;
        private String what;
        private MissionStateEstimate missionStateEstimate;
        private StructureStateEstimate structureStateEstimate;
        
        /**
         * @param mseConverter the MSE converter.
         * @param missionStateEstimate the MSE.
         * @param what the subset of the MSE to be emitted.
         */
        public MseStreamResponse(JsonConverter mseConverter, MissionStateEstimate missionStateEstimate, 
            StructureStateEstimate structureStateEstimate, String what)
        {
            this.mseConverter = mseConverter;
            this.missionStateEstimate = missionStateEstimate;
            this.structureStateEstimate = structureStateEstimate;
            this.what = what;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getContentType()
        {
            return "application/json";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public InputStream getStream() throws IOException
        {
            if (missionStateEstimate == null)
            {
                return new ByteArrayInputStream("".getBytes("UTF-8"));
            }

            JSONCollection m;
            if (what == null)
            {
                m = mseConverter.convertMseAndSseToJSON(missionStateEstimate, structureStateEstimate);
            }
            else if ("locations".equals(what))
            {
                m = mseConverter.convertLocationListToJSON(structureStateEstimate.getLocations());
            }
            else if ("vehicles".equals(what))
            {
                m = mseConverter.convertVehicleListToJSON(structureStateEstimate.getVehicles());
            }
            else
            {
                m = mseConverter.convertMseAndSseToJSON(missionStateEstimate, structureStateEstimate);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
            OutputStreamWriter streamWriter = new OutputStreamWriter(out, "UTF-8");
            PrintWriter writer = new PrintWriter(streamWriter);
            m.print(writer);
            //m.prettyPrint(writer);
            writer.close();
            out.close();
            return new ByteArrayInputStream(out.toByteArray());
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void prepareResponse(final Response response)
        {
            // intentionally empty.
        }
    }
}
