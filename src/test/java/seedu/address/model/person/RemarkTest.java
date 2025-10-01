package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Remark(null));
    }

    @Test
    public void constructor_validRemark_success() {
        Remark remark = new Remark("Valid remark");
        assertEquals("Valid remark", remark.value);
    }

    @Test
    public void constructor_emptyRemark_success() {
        Remark remark = new Remark("");
        assertEquals("", remark.value);
    }

    @Test
    public void equals() {
        Remark remark = new Remark("Hello");

        // same values -> returns true
        assertTrue(remark.equals(new Remark("Hello")));

        // same object -> returns true
        assertTrue(remark.equals(remark));

        // null -> returns false
        assertFalse(remark.equals(null));

        // different types -> returns false
        assertFalse(remark.equals(5.0f));

        // different values -> returns false
        assertFalse(remark.equals(new Remark("Bye")));
    }

    @Test
    public void hashCode_sameValue_sameHashCode() {
        Remark remark1 = new Remark("Test");
        Remark remark2 = new Remark("Test");
        assertEquals(remark1.hashCode(), remark2.hashCode());
    }

    @Test
    public void toString_validRemark_correctString() {
        Remark remark = new Remark("Test remark");
        assertEquals("Test remark", remark.toString());
    }

    @Test
    public void toString_emptyRemark_emptyString() {
        Remark remark = new Remark("");
        assertEquals("", remark.toString());
    }
}
