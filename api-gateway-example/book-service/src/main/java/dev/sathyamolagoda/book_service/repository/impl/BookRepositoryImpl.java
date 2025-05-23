package dev.sathyamolagoda.book_service.repository.impl;

import dev.sathyamolagoda.book_service.model.Book;
import dev.sathyamolagoda.book_service.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class BookRepositoryImpl implements BookRepository {


    private final DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable<Book> getBookTable() {
        String tableName = "book";
        return enhancedClient.table(tableName, TableSchema.fromBean(Book.class));
    }

    @Override
    public Optional<Book> findById(UUID id) {
        Book book = getBookTable().getItem(r -> r.key(k -> k.partitionValue(String.valueOf(id))));
        return Optional.ofNullable(book);
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        getBookTable().scan().items().forEach(books::add);
        return books;
    }

    @Override
    public Book save(Book book) {
        getBookTable().putItem(book);
        return book;
    }

    @Override
    public List<Book> saveAll(List<Book> books) {
        books.forEach(this::save);
        return books;
    }

    @Override
    public void delete(UUID id) {
        getBookTable().deleteItem(r -> r.key(k -> k.partitionValue(id.toString())));
    }
}
