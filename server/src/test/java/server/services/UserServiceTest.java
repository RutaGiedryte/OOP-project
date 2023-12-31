package server.services;

import commons.Board;
import commons.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.api.BoardController;
import server.database.TestBoardRepository;
import server.database.TestMessageChannel;
import server.database.TestSimpMessagingTemplate;
import server.database.TestUserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private TestUserRepository userRepo;
    private TestBoardRepository boardRepo;
    private Board b1;
    private User u1, u2, u3;
    private List<User> users;
    private UserService sut;
    private BoardController boardCtrl;
    private TestSimpMessagingTemplate simp;

    @BeforeEach
    void setUp() {
        simp = new TestSimpMessagingTemplate(new TestMessageChannel());
        userRepo = new TestUserRepository();
        boardRepo = new TestBoardRepository();
        boardCtrl = new BoardController(new BoardService(boardRepo),simp );
        b1 = new Board(0, "First Board", "123", new ArrayList<>());
        boardRepo.save(b1);
        u1 = new User(0, "First User");
        u2 = new User(1, "Second User");
        u3 = new User(2, "Third User");
        users = new ArrayList<>();
        users.add(u1);
        users.add(u2);
        users.add(u3);
        userRepo.setUsers(users);
        sut = new UserService(userRepo,boardCtrl, boardRepo);
    }

    @Test
    void testGetByIdSuccessful() throws Exception {
        ResponseEntity userResponse = sut.getById(0);
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(new User(0, "First User"), userResponse.getBody());
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        expectedCalls.add(userRepo.FIND_BY_ID);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testGetByIdInvalidId() throws Exception {
        assertThrows(Exception.class, () -> sut.getById(-1));
    }

    @Test
    void testGetByIdNonExistent() throws Exception {
        assertThrows(Exception.class, () -> sut.getById(100));
    }

    @Test
    void testAddSuccessful() throws Exception {
        User u4 = new User(3, "Fourth User");
        ResponseEntity userResponse = sut.add(u4);
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(new User(3, "Fourth User"), userResponse.getBody());
        expectedCalls.add(userRepo.SAVE);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testAddInvalidName() throws Exception {
        User u4 = new User(3, null);
        assertThrows(Exception.class, () -> sut.add(u4));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testDeleteByIdSuccessful() throws Exception {
        ResponseEntity userResponse = sut.deleteById(1);
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(new User(1, "Second User"), userResponse.getBody());
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        expectedCalls.add(userRepo.FIND_BY_ID);
        expectedCalls.add(userRepo.DELETE_BY_ID);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testDeleteByIdNonExistent() throws Exception {
        assertThrows(Exception.class, () -> sut.deleteById(100));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testGetByUsernameSuccessful() throws Exception {
        ResponseEntity userResponse = sut.getByUsername("First User");
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(new User(0, "First User"), userResponse.getBody());
        expectedCalls.add(userRepo.FIND_BY_USERNAME);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testGetByUsernameInvalidName() throws Exception {
        assertThrows(Exception.class, () -> sut.getByUsername(""));
        List<String> expectedCalls = new ArrayList<>();
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testJoinBoardSuccessful() throws Exception {
        ResponseEntity userResponse = sut.joinBoard(0, 0);
        assertTrue(b1.users.contains(userResponse.getBody()));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        expectedCalls.add(userRepo.FIND_BY_ID);
        // return final
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        expectedCalls.add(userRepo.FIND_BY_ID);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testJoinBoardInvalidId() throws Exception {
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, () -> sut.joinBoard(0, -1));
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        expectedCalls.add(userRepo.FIND_BY_ID);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testRemoveBoardSuccessful() throws Exception {
        b1.users.add(u1);
        ResponseEntity userResponse = sut.removeBoard(0, 0);
        assertTrue(!b1.users.contains(userResponse.getBody()));
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        expectedCalls.add(userRepo.FIND_BY_ID);
        expectedCalls.add(userRepo.EXISTS_BY_ID);
        expectedCalls.add(userRepo.FIND_BY_ID);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void testRemoveBoardInvalidId() throws Exception {
        b1.users.add(u1);
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, () -> sut.removeBoard(0, -1));
        expectedCalls.add(TestUserRepository.EXISTS_BY_ID);
        expectedCalls.add(TestUserRepository.FIND_BY_ID);
        assertEquals(expectedCalls, userRepo.getCalls());
    }

    @Test
    void getRepo() {
        assertEquals(userRepo, sut.getRepo());
    }
}
