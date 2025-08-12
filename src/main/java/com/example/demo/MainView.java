package com.example.demo;

import com.example.demo.entity.Message;
import com.example.demo.repository.MessageRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout {

    private final MessageRepository messageRepository;
    private final TextArea messageInput;
    private final Button sendButton;
    private final VerticalLayout messagesLayout;

    public MainView(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;

        // Создаем компоненты
        H1 title = new H1("Vaadin Chat Applicationupdate");
        messageInput = new TextArea("Введите сообщение:");
        messageInput.setWidth("400px");
        messageInput.setMinHeight("100px");

        sendButton = new Button("Отправить");
        messagesLayout = new VerticalLayout();
        messagesLayout.setWidth("100%");

        // Настройка кнопки
        sendButton.addClickListener(e -> sendMessage());
        messageInput.addKeyDownListener(com.vaadin.flow.component.Key.ENTER, e -> sendMessage());

        // Компоновка
        HorizontalLayout inputLayout = new HorizontalLayout(messageInput, sendButton);
        inputLayout.setAlignItems(Alignment.END);

        add(title, inputLayout, messagesLayout);

        // Загружаем существующие сообщения
        loadMessages();
    }

    private void sendMessage() {
        String content = messageInput.getValue();
        if (content != null && !content.trim().isEmpty()) {
            Message message = new Message(content.trim());
            messageRepository.save(message);
            messageInput.clear();
            loadMessages(); // Обновляем список сообщений
        }
    }

    private void loadMessages() {
        messagesLayout.removeAll();
        List<Message> messages = messageRepository.findAllByOrderByCreatedAtDesc();

        for (Message message : messages) {
            Div messageDiv = new Div();
            messageDiv.getStyle().set("border", "1px solid #ccc");
            messageDiv.getStyle().set("padding", "10px");
            messageDiv.getStyle().set("margin", "5px 0");
            messageDiv.getStyle().set("border-radius", "5px");

            Paragraph content = new Paragraph(message.getContent());
            content.getStyle().set("margin", "0");

            Paragraph timestamp = new Paragraph(
                    message.getCreatedAt().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))
            );
            timestamp.getStyle().set("font-size", "0.8em");
            timestamp.getStyle().set("color", "#666");
            timestamp.getStyle().set("margin", "5px 0 0 0");

            messageDiv.add(content, timestamp);
            messagesLayout.add(messageDiv);
        }
    }
}