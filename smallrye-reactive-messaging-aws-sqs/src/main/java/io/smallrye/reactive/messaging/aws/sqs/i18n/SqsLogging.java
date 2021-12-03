package io.smallrye.reactive.messaging.aws.sqs.i18n;

import org.jboss.logging.Logger;
import org.jboss.logging.annotations.MessageLogger;

/**
 * Messages for AWS SQS Connector
 * Assigned ID range is 14200-14299
 */
@MessageLogger(projectCode = "SRMSG", length = 5)
public interface SqsLogging {

    SqsLogging log = Logger.getMessageLogger(SqsLogging.class, "io.smallrye.reactive.messaging.aws.sqs");
}
