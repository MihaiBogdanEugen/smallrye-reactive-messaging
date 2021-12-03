package io.smallrye.reactive.messaging.aws.sqs;

import static io.smallrye.reactive.messaging.aws.sqs.i18n.SqsMessages.msg;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Metadata;
import software.amazon.awssdk.services.sqs.model.MessageAttributeValue;
import software.amazon.awssdk.services.sqs.model.MessageSystemAttributeName;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

public class SqsMessage implements Message<String> {

    private final software.amazon.awssdk.services.sqs.model.Message message;

    public SqsMessage(final software.amazon.awssdk.services.sqs.model.Message message) {
        this.message = Objects.requireNonNull(message, msg.isRequired("message"));
    }

    public SqsMessage(final String body) {
        Objects.requireNonNull(body, msg.isRequired("body"));
        this.message = software.amazon.awssdk.services.sqs.model.Message.builder()
                .body(body)
                .build();
    }

    public SqsMessage(final String body, final Map<String, MessageAttributeValue> messageAttributes) {
        Objects.requireNonNull(body, msg.isRequired("body"));
        software.amazon.awssdk.services.sqs.model.Message.Builder builder =
                software.amazon.awssdk.services.sqs.model.Message.builder().body(body);
        if (messageAttributes != null) {
            builder = builder.messageAttributes(messageAttributes);
        }
        this.message = builder.build();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final SqsMessage that = (SqsMessage) o;
        return Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message);
    }

    /**
     * @return The payload for this message.
     */
    @Override
    public final String getPayload() {
        return this.message.body();
    }

    /**
     * @return The set of metadata attached to this message, potentially empty.
     */
    @Override
    public final Metadata getMetadata() {
        return this.message.hasMessageAttributes()
                ? Metadata.of(new SqsMessageAttributes(this.message.messageAttributes()))
                : Message.super.getMetadata();
    }

    /**
     * @return the supplier used to retrieve the acknowledgement {@link CompletionStage}.
     */
    @Override
    public final Supplier<CompletionStage<Void>> getAck() {
        return this::ack;
    }

    /**
     * Acknowledge this message.
     *
     * @return a completion stage completed when the message is acknowledged. If the acknowledgement fails, the
     * completion stage propagates the failure.
     */
    @Override
    public final CompletionStage<Void> ack() {
        //Acknowledgment is handled automatically by AWS SDK.
        return CompletableFuture.completedFuture(null);
    }

    /**
     * A unique identifier for the message. A <code>MessageId</code>is considered unique across all Amazon Web Services
     * accounts for an extended period of time.
     *
     * @return A unique identifier for the message. A <code>MessageId</code>is considered unique across all Amazon Web
     *         Services accounts for an extended period of time.
     */
    public final String messageId() {
        return this.message.messageId();
    }

    /**
     * An identifier associated with the act of receiving the message. A new receipt handle is returned every time you
     * receive a message. When deleting a message, you provide the last received receipt handle to delete the message.
     *
     * @return An identifier associated with the act of receiving the message. A new receipt handle is returned every
     *         time you receive a message. When deleting a message, you provide the last received receipt handle to
     *         delete the message.
     */
    public final String getReceiptHandle() {
        return this.message.receiptHandle();
    }

    /**
     * An MD5 digest of the non-URL-encoded message body string.
     *
     * @return An MD5 digest of the non-URL-encoded message body string.
     */
    public final String getMd5OfBody() {
        return this.message.md5OfBody();
    }

    public final Optional<String> getSenderId() {
        return getMessageSystemAttribute(MessageSystemAttributeName.SENDER_ID);
    }

    public final Optional<String> getSentTimestamp() {
        return getMessageSystemAttribute(MessageSystemAttributeName.SENT_TIMESTAMP);
    }

    public final Optional<String> getApproximateReceiveCount() {
        return getMessageSystemAttribute(MessageSystemAttributeName.APPROXIMATE_RECEIVE_COUNT);
    }

    public final Optional<String> getApproximateFirstReceiveTimestamp() {
        return getMessageSystemAttribute(MessageSystemAttributeName.APPROXIMATE_FIRST_RECEIVE_TIMESTAMP);
    }

    public final Optional<String> getSequenceNumber() {
        return getMessageSystemAttribute(MessageSystemAttributeName.SEQUENCE_NUMBER);
    }

    public final Optional<String> getMessageDeduplicationId() {
        return getMessageSystemAttribute(MessageSystemAttributeName.MESSAGE_DEDUPLICATION_ID);
    }

    public final Optional<String> getMessageGroupId() {
        return getMessageSystemAttribute(MessageSystemAttributeName.MESSAGE_GROUP_ID);
    }

    public final Optional<String> getAWSTraceHeader() {
        return getMessageSystemAttribute(MessageSystemAttributeName.AWS_TRACE_HEADER);
    }

    private Optional<String> getMessageSystemAttribute(final MessageSystemAttributeName attributeName) {
        return this.message.hasAttributes() && this.message.attributes().containsKey(attributeName)
                ? Optional.of(this.message.attributes().get(attributeName))
                : Optional.empty();
    }

    public final static class SqsMessageAttributes {

        private Map<String, MessageAttributeValue> attributes;

        public SqsMessageAttributes() {
        }

        public SqsMessageAttributes(final Map<String, MessageAttributeValue> attributes) {
            this.attributes = attributes;
        }

        public Map<String, MessageAttributeValue> getAttributes() {
            return attributes;
        }

        public void setAttributes(final Map<String, MessageAttributeValue> attributes) {
            this.attributes = attributes;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final SqsMessageAttributes that = (SqsMessageAttributes) o;
            return Objects.equals(attributes, that.attributes);
        }

        @Override
        public int hashCode() {
            return Objects.hash(attributes);
        }
    }
}
