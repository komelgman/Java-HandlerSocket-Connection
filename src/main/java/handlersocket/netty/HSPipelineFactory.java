/*
 * Copyright 2011 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package kom.handlersocket.netty;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPipelineFactory;
import io.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.frame.Delimiters;
import io.netty.handler.codec.string.StringEncoder;
import kom.handlersocket.HSConnection;

import static io.netty.channel.Channels.pipeline;

/**
 * Creates a newly configured {@link io.netty.channel.ChannelPipeline} for a new channel.
 */
public class HSPipelineFactory implements
        ChannelPipelineFactory {

	private final HSConnection connection;

	public HSPipelineFactory(HSConnection connection) {
		this.connection = connection;
	}

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = pipeline();

        // Add the text line codec combination first,
        pipeline.addLast("packetCapture", new HSPacketCapture());

        pipeline.addLast("decoder", new HSDecoder());
        pipeline.addLast("encoder", new HSEncoder(connection.getCharset()));

        // and then business logic.
        pipeline.addLast("handler", new HSHandler(connection));

        return pipeline;
    }
}
