package org.example.test.onpier.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.example.test.onpier.dto.BorrowedCsvDto;
import org.example.test.onpier.model.Book;
import org.example.test.onpier.model.Borrowed;
import org.example.test.onpier.model.User;
import org.example.test.onpier.model.UserId;
import org.example.test.onpier.repository.BookRepository;
import org.example.test.onpier.repository.BorrowedRepository;
import org.example.test.onpier.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvReader {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private BookRepository bookRepository;

    private UserRepository userRepository;

    private BorrowedRepository borrowedRepository;

    @Autowired
    public CsvReader(BookRepository bookRepository, UserRepository userRepository, BorrowedRepository borrowedRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.borrowedRepository = borrowedRepository;
    }

    @PostConstruct
    private void prepareDataPostConstruct(){
        System.out.println("started to load csvs...");
        this.uploadCsvToDb();
        System.out.println("finished to load csvs...");
    }

    @Transactional
    public void uploadCsvToDb() {
        List<User> users;
        Set<Book> books;
        List<BorrowedCsvDto> borrowedCsvDtoList;

        try {
            users = readUsers();
            books = readBooks();
            borrowedCsvDtoList = readBorrowed();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        List<Borrowed> finalBorrowedList = mergeCsvsToBorrowedList(users, books, borrowedCsvDtoList);


        userRepository.saveAll(users);
        bookRepository.saveAll(books);
        borrowedRepository.saveAll(finalBorrowedList);
    }


    public static List<User> readUsers() throws FileNotFoundException {
        File userFile = new File("src/main/resources/user.csv");
        InputStream is = new FileInputStream(userFile);
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(bReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<User> userList = new ArrayList<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                String name = csvRecord.get("Name");
                String firstName = csvRecord.get("First name");
                UserId userId = new UserId(name, firstName);
                String memberSince = csvRecord.get("Member since");
                String memberTill = csvRecord.get("Member till");
                String gender = csvRecord.get("Gender");
                Date memberSinceDate;
                Date memberTillDate;
                try {
                    memberSinceDate = DATE_FORMAT.parse(memberSince);
                } catch (ParseException e) {
                    memberSinceDate = null;
                }
                try {
                    memberTillDate = DATE_FORMAT.parse(memberTill);
                } catch (ParseException e) {
                    memberTillDate = null;
                }
                User user = User.builder().id(userId).memberSince(memberSinceDate).memberTill(memberTillDate).gender(gender).build();
                userList.add(user);
            }
            return userList;
        } catch (IOException e) {
            throw new RuntimeException("users csv failed to parse");
        }
    }

    public static Set<Book> readBooks() throws FileNotFoundException {
        File booksFile = new File("src/main/resources/books.csv");
        InputStream is = new FileInputStream(booksFile);
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(bReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            Set<Book> bookList = new HashSet<>();
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                String title = csvRecord.get("Title");
                String author = csvRecord.get("Author");
                String genre = csvRecord.get("Genre");
                String publisher = csvRecord.get("Publisher");
                Book book = Book.builder()
                        .title(title)
                        .author(author)
                        .genre(genre)
                        .publisher(publisher)
                        .build();
                bookList.add(book);
            }
            return bookList;
        } catch (IOException e) {
            throw new RuntimeException("books csv failed to parse");
        }
    }

    public static List<BorrowedCsvDto> readBorrowed() throws FileNotFoundException {
        File borrowedFile = new File("src/main/resources/borrowed.csv");
        InputStream is = new FileInputStream(borrowedFile);
        try (BufferedReader bReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(bReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<BorrowedCsvDto> borrowedList = new ArrayList<>();
            List<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                String borrowerName = csvRecord.get("Borrower");
                String bookBorrowed = csvRecord.get("Book");
                String borrowedFrom = csvRecord.get("borrowed from");
                String borrowedTo = csvRecord.get("borrowed to");
                Date borrowedFromDate;
                Date borrowedToDate;
                try {
                    borrowedFromDate = DATE_FORMAT.parse(borrowedFrom);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                try {
                    borrowedToDate = DATE_FORMAT.parse(borrowedTo);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                BorrowedCsvDto borrowedCsvDto = BorrowedCsvDto.builder()
                        .borrowerName(borrowerName)
                        .bookName(bookBorrowed)
                        .borrowedFrom(borrowedFromDate)
                        .borrowedTo(borrowedToDate)
                        .build();
                borrowedList.add(borrowedCsvDto);
            }
            return borrowedList;
        } catch (IOException e) {
            throw new RuntimeException("books csv failed to parse");
        }
    }

    private static List<Borrowed> mergeCsvsToBorrowedList(List<User> users, Set<Book> books, List<BorrowedCsvDto> borrowedCsvDtoList) {
        List<Borrowed> finalBorrowedList = borrowedCsvDtoList.stream().map(csv -> {
            UserId userId = parseUserIdFromBorrowedCsvDto(csv);
            Optional<User> userOpt = users.stream().filter(u -> u.getId().equals(userId)).findAny();
            Optional<Book> bookOpt = books.stream().filter(b -> b.getTitle().equals(csv.getBookName())).findAny();
            if (userOpt.isEmpty() || bookOpt.isEmpty()) {
                throw new RuntimeException("merging csvs exception");
            }
            User user = userOpt.get();
            Book book = bookOpt.get();

            Borrowed borrowed = Borrowed.builder().borrowedFrom(csv.getBorrowedFrom())
                    .borrowedTo(csv.getBorrowedTo())
                    .user(user)
                    .book(book)
                    .build();
            if (user.getBorrowingHistory() != null) {
                user.getBorrowingHistory().add(borrowed);
            } else {
                user.setBorrowingHistory(new ArrayList<>(Collections.singletonList(borrowed)));
            }
            if(book.getBorrowedHistory() != null){
                book.getBorrowedHistory().add(borrowed);
            } else {
                book.setBorrowedHistory(new ArrayList<>(Collections.singletonList(borrowed)));
            }
            return borrowed;
        }).collect(Collectors.toList());
        return finalBorrowedList;
    }

    private static UserId parseUserIdFromBorrowedCsvDto(BorrowedCsvDto csv) {
        String[] splitName = csv.getBorrowerName().split(",");
        String name = splitName[0];
        String firstName = splitName[1];
        return new UserId(name,firstName);
    }
}
