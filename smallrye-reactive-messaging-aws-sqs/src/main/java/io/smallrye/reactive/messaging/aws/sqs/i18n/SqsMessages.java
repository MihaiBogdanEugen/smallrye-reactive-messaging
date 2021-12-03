package io.smallrye.reactive.messaging.aws.sqs.i18n;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;

/**
 * Messages for AWS SQS Connector
 * Assigned ID range is 14100-14199
 */
@MessageBundle(projectCode = "SRMSG", length = 5)
public interface SqsMessages {

    SqsMessages msg = Messages.getBundle(SqsMessages.class);

    @Message(id = 14100, value = "%s is required")
    String isRequired(String fieldName);
}
