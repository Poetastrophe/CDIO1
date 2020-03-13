import org.junit.jupiter.api.Test;

public class test {

    @Test
    public void testAll(){
        NonPersistentTest nonPersistentTest = new NonPersistentTest();
        SQLTest sqlTest = new SQLTest("root","password");
    }

}

