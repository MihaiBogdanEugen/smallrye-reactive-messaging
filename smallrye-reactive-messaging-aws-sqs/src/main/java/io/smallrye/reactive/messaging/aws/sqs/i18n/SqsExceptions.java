package io.smallrye.reactive.messaging.aws.sqs.i18n;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.MessageBundle;

/**
 * Messages for AWS SQS Connector
 * Assigned ID range is 14000-14099
 */
@MessageBundle(projectCode = "SRMSG", length = 5)
public interface SqsExceptions {

    SqsExceptions ex = Messages.getBundle(SqsExceptions.class);
}
