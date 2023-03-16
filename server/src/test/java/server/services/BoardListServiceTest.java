package server.services;

import commons.Board;
import commons.BoardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.TestBoardListRepository;
import server.database.TestBoardRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardListServiceTest {

    private TestBoardListRepository listRepo;
    private TestBoardRepository boardRepo;
    private BoardListService sut;
    private Board b1;
    private BoardList l1, l2, l3;
    private List<BoardList> boardLists;

    @BeforeEach
    void setUp() {
        listRepo = new TestBoardListRepository();
        boardRepo = new TestBoardRepository();
        b1 = new Board(0, "Main Board", "123", new ArrayList<>());
        boardRepo.save(b1);
        l1 = new BoardList(1, "First", b1, 0);
        l2 = new BoardList(2, "Second", b1, 0);
        l3 = new BoardList(3, "Third", b1, 0);
        boardLists = new ArrayList<>();
        boardLists.add(l1);
        boardLists.add(l2);
        boardLists.add(l3);
        listRepo.setBoardLists(boardLists);
        sut = new BoardListService(listRepo, boardRepo);
    }

    @Test
    public void constructorTest() {
        assertNotNull(sut);
    }

    @Test
    void testFindAll() {
        List<BoardList> result = sut.findAll();
        List<BoardList> expected = new ArrayList<>();
        expected.add(l1);
        expected.add(l2);
        expected.add(l3);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.FIND_ALL);
        assertEquals(expected, result);
        assertEquals(expectedCalls, listRepo.getCalls());
    }

    @Test
    void testAddSuccessful() throws Exception {
        BoardList l4 = new BoardList(4, "Fourth", b1, 0);
        BoardList expected = sut.add(l4);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.SAVE);
        assertEquals(expected, l4);
        assertEquals(expectedCalls, listRepo.getCalls());
    }

    @Test
    public void testAddInvalidTitle() throws Exception {
        BoardList l4 = new BoardList(4, null, b1, 0);
        List<String> expectedCalls = new ArrayList<>();
        assertThrows(Exception.class, () -> sut.add(l4));
        assertEquals(expectedCalls, listRepo.getCalls());
    }

    @Test
    public void testDeleteByIdSuccess() throws Exception {
        BoardList result = sut.deleteById(2);
        List<String> expectedCalls = new ArrayList<>();
        expectedCalls.add(TestBoardRepository.EXISTS_BY_ID);
        expectedCalls.add(TestBoardRepository.FIND_BY_ID);
        expectedCalls.add(TestBoardRepository.DELETE_BY_ID);
        assertEquals(l2, result);
        assertEquals(expectedCalls, listRepo.getCalls());
    }

    @Test
    void updateTitleById() {
    }
}