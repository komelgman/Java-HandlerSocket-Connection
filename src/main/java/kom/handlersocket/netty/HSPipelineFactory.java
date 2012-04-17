/*
 * Copyright 2012 The Java HandlerSocket Connection Project
 *
 * https://github.com/komelgman/Java-HandlerSocket-Connection/
 *
 * The Project licenses this file to you under the Apache License,
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

import kom.handlersocket.HSConnection;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.execution.ExecutionHandler;

import static org.jboss.netty.channel.Channels.pipeline;

/**
 * Creates a newly configured {@link org.jboss.netty.channel.ChannelPipeline} for a new channel.
 */
public class HSPipelineFactory implements ChannelPipelineFactory {

	private final HSConnection connection;

	public HSPipelineFactory(HSConnection connection) {
		this.connection = connection;
	}

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();

		pipeline.addLast("decoder", new HSDecoder());
		pipeline.addLast("encoder", new HSEncoder());

		pipeline.addLast("logic", new HSHandler(connection));

		return pipeline;
	}
}
