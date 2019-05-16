//package test;
//
//import model.enemies.Enemy;
//import model.GameModel;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.test;
//
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//public class GameModelTest {
//
//    GameModel gm;
//
//    @BeforeEach
//    void setUp() {
//        gm = new GameModel();
//    }
//
//    @test
//    public void testMoveableDirectOverlap() {
//        Enemy e1 = new Enemy(50, 50);
//        Enemy e2 = new Enemy(50, 50);
//        assertTrue(gm.coordinatesOverlap(e1, e2));
//    }
//
//    @test
//    public void testMoveableNoOverlapSameX() {
//        Enemy e1 = new Enemy(50, 50);
//        Enemy e2 = new Enemy(50, 500);
//        assertFalse(gm.coordinatesOverlap(e1, e2));
//    }
//
//    @test
//    public void testMoveableNoOverlapSameY() {
//        Enemy e1 = new Enemy(50, 50);
//        Enemy e2 = new Enemy(500, 50);
//        assertFalse(gm.coordinatesOverlap(e1, e2));
//    }
//
//    @test
//    public void testMoveableOverlapOppositeBottomEdge() {
//        Enemy e1 = new Enemy(500, 500);
//        Enemy e2 = new Enemy(500 + Enemy.SIZE_X, 500 + Enemy.SIZE_Y);
//        assertTrue(gm.coordinatesOverlap(e1, e2));
//    }
//
//    @test
//    public void testMoveableNoOverlapOppositeBottomEdgeBeyond() {
//        Enemy e1 = new Enemy(500, 500);
//        Enemy e2 = new Enemy(500 + Enemy.SIZE_X + 1, 500 + Enemy.SIZE_Y + 1);
//        assertFalse(gm.coordinatesOverlap(e1, e2));
//    }
//
//}
