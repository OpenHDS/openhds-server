package org.openhds.service.impl;

import org.openhds.service.WebFlowService;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.message.MessageResolver;

public class WebFlowServiceImpl implements WebFlowService {
	
	/**
	 * Create a message that can be passed back and used by facelets
	 * NOTE: this is spring specific - refer to the spring-binding documentation
	 * @param messageContext
	 * @param message
	 */
	public void createMessage(MessageContext messageContext, String message) {
		MessageBuilder messageBuilder = new MessageBuilder();
		MessageResolver resolver = messageBuilder.error().defaultText(message).build();
		messageContext.addMessage(resolver);
	}    

}
