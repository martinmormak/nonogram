import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import core.*;
import service.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        ColorTileTest.class,
        CommentServiceJDBCTest.class,
        CommentServiceJPATest.class,
        CommentTest.class,
        FieldTest.class,
        FiledGeneratorTest.class,
        NumberTileTest.class,
        RatingServiceJDBCTest.class,
        RatingTest.class,
        ScoreServiceJDBCTest.class,
        ScoreTest.class
})
public class AllTests {
}
