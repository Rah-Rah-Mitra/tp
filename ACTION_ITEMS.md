# Code Review Summary - Action Items

## Overview
This document provides specific, actionable recommendations based on the comprehensive code scan report. Each item includes the location, issue, and suggested fix.

---

## 🔴 HIGH PRIORITY ITEMS

### 1. Complete Timezone Implementation in Person.java

**File:** `src/main/java/seedu/address/model/person/Person.java`

#### Issue 1.1: Timezone not validated in constructor (Line 33-34)
**Current Code:**
```java
//todo ck: check who is sending null Timezone
requireAllNonNull(name, phone, email, address, tags);
```

**Recommended Fix:**
```java
requireAllNonNull(name, phone, email, address, tags, timezone);
```

#### Issue 1.2: Timezone excluded from equals() (Line 105-106)
**Current Code:**
```java
&& tags.equals(otherPerson.tags);
//todo ck: fix equality checks
// && timezone.equals(otherPerson.timezone);
```

**Recommended Fix:**
```java
&& tags.equals(otherPerson.tags)
&& timezone.equals(otherPerson.timezone);
```

#### Issue 1.3: Timezone excluded from toString() (Line 123-125)
**Current Code:**
```java
.add("tags", tags)
.toString();
//todo ck: add this back
//.add("timezone", timezone)
```

**Recommended Fix:**
```java
.add("tags", tags)
.add("timezone", timezone)
.toString();
```

---

### 2. Complete EditCommand Timezone Handling

**File:** `src/main/java/seedu/address/logic/commands/EditCommand.java`

#### Issue 2.1: Hardcoded timezone in createEditedPerson (Line 105-107)
**Current Code:**
```java
//todo ck: continue implementing
return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags,
        new Timezone(Timezone.NO_TIMEZONE));
```

**Recommended Implementation:**
```java
Timezone updatedTimezone = editPersonDescriptor.getTimezone().orElse(personToEdit.getTimezone());
return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags, updatedTimezone);
```

#### Issue 2.2: Add timezone field to EditPersonDescriptor (after line 143)
**Add the following to EditPersonDescriptor class:**
```java
private Timezone timezone;

public void setTimezone(Timezone timezone) {
    this.timezone = timezone;
}

public Optional<Timezone> getTimezone() {
    return Optional.ofNullable(timezone);
}
```

#### Issue 2.3: Update isAnyFieldEdited method (Line 162-163)
**Current Code:**
```java
return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
```

**Recommended Fix:**
```java
return CollectionUtil.isAnyNonNull(name, phone, email, address, tags, timezone);
```

---

### 3. Remove or Implement Unused Method in Timezone.java

**File:** `src/main/java/seedu/address/model/person/Timezone.java`

#### Issue 3.1: Unimplemented method (Line 21-23)
**Current Code:**
```java
public String getTzOffset(Timezone other) {
    return "NOT IMPLEMENTED";
}
```

**Option A - Implement (if needed):**
```java
/**
 * Calculates the time difference between this timezone and another timezone.
 * @param other The other timezone to compare with
 * @return A string representation of the time difference
 */
public String getTzOffset(Timezone other) {
    if (this.tzOffset == NO_TIMEZONE || other.tzOffset == NO_TIMEZONE) {
        return "Unknown timezone difference";
    }
    double difference = this.tzOffset - other.tzOffset;
    return String.format("%.2f hours", difference);
}
```

**Option B - Remove (if not needed):**
```java
// Delete the method entirely if it's not being used anywhere
```

**Recommendation:** Check if this method is called anywhere in the codebase. If not, remove it to reduce dead code.

---

## 🟡 MEDIUM PRIORITY ITEMS

### 4. Enhance SortCommand with Parameters

**File:** `src/main/java/seedu/address/logic/commands/SortCommand.java`

#### Issue 4.1: TODO comment indicates incomplete feature (Line 11)
**Current Code:**
```java
// TODO Add sort as a param e.g. s/ (will need to edit clisyntax.java)
```

**Options:**
1. **Implement the feature:** Add parameters for sorting by different fields (name, phone, email, etc.)
2. **Remove the TODO:** If the basic sort by name is sufficient, remove the comment
3. **Create a separate issue:** Document this as a future enhancement and remove the inline TODO

**Recommended Action:** Create a separate issue in your issue tracker for this enhancement and remove the inline TODO comment.

---

### 5. Improve Exception Documentation

**File:** `src/main/java/seedu/address/model/person/exceptions/PersonNotFoundException.java`

#### Issue 5.1: Minimal documentation (Line 4-6)
**Current Code:**
```java
/**
 * Signals that the operation is unable to find the specified person.
 */
public class PersonNotFoundException extends RuntimeException {}
```

**Recommended Enhancement:**
```java
/**
 * Signals that the operation is unable to find the specified person.
 * This exception is thrown when attempting to access, modify, or delete a person
 * that does not exist in the address book.
 */
public class PersonNotFoundException extends RuntimeException {
    public PersonNotFoundException() {
        super("Person not found in the address book");
    }
}
```

---

## 🟢 LOW PRIORITY ITEMS

### 6. Improve Git Commit Message Quality

**Current Issues:**
- Vague messages like "Initial plan"
- Missing context and scope

**Recommended Format:**
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Examples of Good Commit Messages:**
```
feat(filter): add case-sensitive tag filtering

- Implement TagContainsKeywordsPredicate for exact tag matching
- Update FilterCommand to use new predicate
- Tags must match exactly (case-sensitive)

Closes #123

test(filter): add comprehensive test cases for FilterCommand

- Add test for case mismatch scenarios
- Add test for missing tags
- Add test for multiple tag filtering
- Ensure empty tag list returns no results

Related to #123

refactor(person): complete timezone implementation

- Add timezone to equals() method
- Add timezone to toString() output
- Validate timezone in constructor
- Update EditCommand to handle timezone edits

Fixes #456
```

**Commit Type Keywords:**
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, no logic change)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks
- `perf`: Performance improvements

---

## 🎯 TESTING CHECKLIST

After implementing the high-priority fixes, ensure:

### Person.java Tests
- [ ] Test that constructor rejects null timezone
- [ ] Test that equals() correctly compares timezone
- [ ] Test that toString() includes timezone
- [ ] Test timezone with NO_TIMEZONE value

### EditCommand Tests
- [ ] Test editing person with timezone
- [ ] Test that timezone is preserved when not edited
- [ ] Test EditPersonDescriptor with timezone field
- [ ] Test isAnyFieldEdited() includes timezone

### Timezone.java Tests (if getTzOffset is implemented)
- [ ] Test getTzOffset with valid timezones
- [ ] Test getTzOffset with NO_TIMEZONE
- [ ] Test getTzOffset with negative offsets
- [ ] Test getTzOffset with positive offsets

---

## 📊 Implementation Order

1. **Day 1:** Fix Person.java timezone issues (Items 1.1, 1.2, 1.3)
2. **Day 1-2:** Update EditCommand timezone handling (Items 2.1, 2.2, 2.3)
3. **Day 2:** Decide on Timezone.getTzOffset() method (Item 3.1)
4. **Day 3:** Write comprehensive tests for all changes
5. **Day 3:** Address medium priority items (Items 4, 5)
6. **Ongoing:** Improve git commit messages (Item 6)

---

## 📝 Notes

- All changes should be made incrementally with proper testing
- Run `./gradlew build` and `./gradlew test` after each change
- Update relevant documentation after completing fixes
- Consider adding integration tests for timezone functionality
- Review with team before merging major changes

---

## ✅ Definition of Done

A task is complete when:
1. Code changes are implemented
2. Unit tests are written and passing
3. Build passes with no warnings
4. Code is reviewed (self-review at minimum)
5. Documentation is updated
6. Commit message follows guidelines
7. Changes are tested manually (if applicable)

---

**Last Updated:** 2025-10-15  
**Next Review:** After implementing high-priority items
