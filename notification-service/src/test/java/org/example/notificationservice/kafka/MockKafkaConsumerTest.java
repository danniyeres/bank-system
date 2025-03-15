package org.example.notificationservice.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.example.notificationservice.kafka.AuthListener;
import org.example.notificationservice.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MockKafkaConsumerTest {

    private MockConsumer<String, String> mockConsumer;
    private NotificationService notificationService;
    private AuthListener authListener;

    @BeforeEach
    void setup() {
        mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        notificationService = mock(NotificationService.class);
        authListener = new AuthListener(notificationService);
        mockConsumer.subscribe(List.of("auth_events"));
    }

    @Test
    void testAuthListenerWithMockConsumer() {
        String topic = "test-topic";
        TopicPartition partition = new TopicPartition(topic, 0);

        // Создаём MockConsumer
        MockConsumer<String, String> mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);

        // Назначаем партиции вручную
        mockConsumer.assign(Collections.singletonList(partition));

        // Устанавливаем начальные offset'ы (чтобы избежать ошибки)
        mockConsumer.updateBeginningOffsets(Collections.singletonMap(partition, 0L));
        mockConsumer.updateEndOffsets(Collections.singletonMap(partition, 1L));

        // Добавляем тестовое сообщение
        mockConsumer.addRecord(new ConsumerRecord<>(topic, 0, 0L, "key", "value"));

        // Вызываем poll(), теперь ошибки не будет
        ConsumerRecords<String, String> records = mockConsumer.poll(Duration.ofMillis(100));

        // Проверяем, что сообщение прочитано
        assertEquals(1, records.count());
    }


}
