# Functionality Verification Report

**Date:** 2025-10-26  
**Product:** CustomerRelationBook (CRB) / AddressBook Level 3  
**Version:** Current development version  
**Purpose:** Verify functionality against DeveloperGuide requirements

---

## Executive Summary

This report documents the verification of the CustomerRelationBook application against the requirements specified in the DeveloperGuide. The verification was conducted through code inspection, examining implementation details, and comparing them with documented requirements.

### Key Findings:
- **Major Discrepancies:** 2 critical issues found
- **Undocumented Features:** 1 feature implemented but not documented
- **Proposed Features:** 2 features documented but not implemented
- **Documentation Gaps:** Multiple user stories and use cases not fully reflected in documentation

---

## 1. Implemented vs. Documented Features

### 1.1 Fully Implemented and Documented Features ✓

| Feature | Command | Status | Notes |
|---------|---------|--------|-------|
| Add Contact | `add` | ✓ PASS | Fully functional with name, phone, email, address, tags |
| List Contacts | `list` | ✓ PASS | Shows all contacts |
| Edit Contact | `edit` | ✓ PASS | Can edit all fields |
| Find Contact | `find` | ✓ PASS | Search by name keywords |
| Delete Contact | `delete` | ✓ PASS | Delete by index |
| Clear All | `clear` | ✓ PASS | Removes all contacts |
| Help | `help` | ✓ PASS | Shows help information |
| Exit | `exit` | ✓ PASS | Exits application |
| Filter by Tags | `filter` | ✓ PASS | Filter contacts by tags (up to 10 tags) |

### 1.2 Undocumented Features Found ⚠️

#### **Issue #1: Timezone Field Not Documented**

**Severity:** HIGH  
**Type:** Documentation Gap

**Description:**
The `Person` class includes a `Timezone` field that stores a UTC offset (e.g., UTC+8, UTC-5). This field is:
- Implemented in the code (`src/main/java/seedu/address/model/person/Timezone.java`)
- Available in the Add command with prefix `tz/`
- Stored in the JSON data file
- NOT mentioned in the DeveloperGuide
- NOT mentioned in the UserGuide

**Evidence:**
```java
// From Person.java
private final Timezone timezone;

// From CliSyntax.java
public static final Prefix PREFIX_TIMEZONE = new Prefix("tz/");

// From AddCommandParser.java
Timezone timezone = ParserUtil.parseTimezone(argMultimap.getValue(PREFIX_TIMEZONE).orElse(""));
```

**Expected Behavior:**
- DeveloperGuide should document the Timezone field in:
  - Architecture diagrams showing Person attributes
  - Use Case UC01 (Add a New Contact)
  - Model component description
- UserGuide should document the `tz/` parameter in the `add` command

**Impact:**
- Users unaware of timezone functionality
- Incomplete understanding of the data model
- Missing from use cases and user stories

**Recommendation:**
Add comprehensive documentation for the Timezone feature including:
1. Purpose and use case
2. Command syntax (`tz/OFFSET` where OFFSET is -24.0 to +24.0)
3. Model diagram updates
4. Example usage in manual testing section

---

### 1.3 Partially Implemented Features ⚠️

#### **Issue #2: Sort Command Not Fully Implemented**

**Severity:** MEDIUM  
**Type:** Implementation Gap

**Description:**
The `sort` command exists in the codebase and appears in the command list, but contains a TODO comment and returns a message indicating it's "not implemented yet".

**Evidence:**
```java
// From SortCommand.java (line 11-16)
public static final String MESSAGE_SUCCESS = "Sorted all persons";
public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Sort command not implemented yet!";

// However, the execute method calls model.sortFilteredPersonList()
@Override
public CommandResult execute(Model model) {
    requireNonNull(model);
    model.sortFilteredPersonList();
    return new CommandResult(MESSAGE_SUCCESS);
}
```

**Actual Status:**
Upon closer inspection, the sort functionality IS implemented:
- `Model.sortFilteredPersonList()` exists and works
- Sorts persons by name (first character)
- Returns success message

**Issues Found:**
1. Conflicting constant `MESSAGE_NOT_IMPLEMENTED_YET` exists but is not used
2. DeveloperGuide mentions this in User Stories (Priority `*` - Low priority: "As a normal user, I want to sort my contacts")
3. UserGuide does NOT document the `sort` command
4. TODO comment in code suggests incomplete implementation

**Impact:**
- Users unaware of sort functionality
- Unclear if feature is complete or in development
- Inconsistent messaging in codebase

**Recommendation:**
1. Remove the `MESSAGE_NOT_IMPLEMENTED_YET` constant if sort is working
2. Remove TODO comment if implementation is complete
3. Add `sort` command to UserGuide command summary
4. Add manual testing instructions for sort command
5. Document sort behavior (alphabetical by name)

---

### 1.4 Proposed But Not Implemented Features ℹ️

#### **Issue #3: Undo/Redo Feature Not Implemented**

**Severity:** LOW (Documented as Proposed)  
**Type:** Planned Feature

**Description:**
The DeveloperGuide contains extensive documentation for an Undo/Redo feature under section "\[Proposed\] Undo/redo feature" including:
- Detailed implementation design
- Sequence diagrams
- Activity diagrams
- Design considerations
- `VersionedAddressBook` class structure

**Status:** NOT IMPLEMENTED
- No `VersionedAddressBook` class exists in codebase
- No `UndoCommand` or `RedoCommand` classes exist
- No undo/redo functionality available to users

**Verification:**
```bash
# No undo/redo commands found
$ grep -r "class UndoCommand" src/
$ grep -r "class RedoCommand" src/
$ grep -r "VersionedAddressBook" src/
# All return no results
```

**Impact:**
- Feature is clearly marked as "\[Proposed\]" so this is expected
- Documentation serves as design specification for future implementation

**Recommendation:**
- No action needed, properly documented as proposed
- Consider adding to product roadmap or future releases section

---

#### **Issue #4: Data Archiving Feature Not Implemented**

**Severity:** LOW (Documented as Proposed)  
**Type:** Planned Feature

**Description:**
The DeveloperGuide mentions "\[Proposed\] Data archiving" but provides no implementation details.

**Status:** NOT IMPLEMENTED
- Placeholder exists in DeveloperGuide: "_{Explain here how the data archiving feature will be implemented}_"
- No archiving functionality in codebase
- UserGuide mentions "Archiving data files `[coming in v2.0]`"

**Impact:**
- Clearly marked as future feature
- UserGuide acknowledges this as coming in v2.0

**Recommendation:**
- No action needed, properly documented as future feature
- Remove placeholder or add planned design when available

---

## 2. Use Case Verification

### 2.1 UC01 - Add a New Contact

**Status:** ✓ MOSTLY IMPLEMENTED

**Requirements Check:**
- [x] User can add new contact with name, phone, email, address
- [x] AddressBook parses contact details
- [x] AddressBook validates each field
- [x] AddressBook checks for duplicates (by name)
- [x] AddressBook saves and confirms
- [x] Error messages for invalid data
- [x] Error messages for missing/wrong fields
- [x] Duplicate detection message

**Issues:**
- ⚠️ Timezone field exists but not documented in UC01
- ⚠️ Duplicate detection based on name only (not documented if this is intentional)

**Code Evidence:**
```java
// From Person.java (line 75-82)
public boolean isSamePerson(Person otherPerson) {
    // Duplicate check uses name only
    return otherPerson != null
            && otherPerson.getName().equals(getName());
}
```

**Impact:** 
Two persons with same name but different phone/email are considered duplicates. This may or may not be desired behavior but is not explicitly documented.

---

### 2.2 UC02 - List Contacts

**Status:** ✓ FULLY IMPLEMENTED

**Requirements Check:**
- [x] User requests to list all contacts
- [x] AddressBook parses command
- [x] AddressBook validates command format
- [x] AddressBook retrieves all saved contacts
- [x] AddressBook displays all contacts
- [x] Console message indicates number of contacts
- [x] Empty list scenario handled

**No Issues Found**

---

### 2.3 UC03 - Add Tags to Contact

**Status:** ✓ FULLY IMPLEMENTED

**Requirements Check:**
- [x] Tags included when adding new contact
- [x] AddressBook parses tag values
- [x] AddressBook validates tag values
- [x] Tags associated with contact
- [x] Error message for invalid tags
- [x] User can correct or remove invalid tags

**Code Evidence:**
```java
// Tag validation allows only alphanumeric characters
// Multiple tags supported with t/ prefix
```

**No Issues Found**

---

### 2.4 UC04 - Filter Contacts by Tags

**Status:** ✓ FULLY IMPLEMENTED

**Requirements Check:**
- [x] User can filter by specified tags
- [x] AddressBook parses tag values
- [x] AddressBook validates tags
- [x] Retrieves contacts with specified tags
- [x] Lists filtered contacts
- [x] Console message shows count
- [x] Error for no tag provided
- [x] Error for invalid tags
- [x] Limit of 10 tags enforced
- [x] No matches handled properly

**Code Evidence:**
```java
// From FilterCommand.java
public static final String MESSAGE_EXCESSIVE_TAGS = "Error: You can only filter by up to 10 tags.";
```

**Issues:**
- ⚠️ UserGuide does NOT document the `filter` command at all
- ⚠️ 10-tag limit not mentioned in UserGuide

---

### 2.5 UC05 - Delete a Person

**Status:** ✓ FULLY IMPLEMENTED

**Requirements Check:**
- [x] User requests to list persons
- [x] AddressBook shows list
- [x] User requests to delete by index
- [x] AddressBook deletes person
- [x] Empty list handled
- [x] Invalid index handled with error

**No Issues Found**

---

## 3. User Stories Verification

### 3.1 High Priority (Must Have) - `* * *`

| User Story | Implementation Status | Notes |
|------------|----------------------|-------|
| See usage instructions | ✓ IMPLEMENTED | `help` command available |
| Add a new person | ✓ IMPLEMENTED | `add` command with all fields |
| Delete a person | ✓ IMPLEMENTED | `delete` command by index |
| Find a person by name | ✓ IMPLEMENTED | `find` command |
| View all my contacts | ✓ IMPLEMENTED | `list` command |
| Filter contacts by tags | ✓ IMPLEMENTED | `filter` command, but NOT in UserGuide |
| Exit the application | ✓ IMPLEMENTED | `exit` command |
| Automatic data saving | ✓ IMPLEMENTED | JSON storage after each change |
| Use consistent commands | ✓ IMPLEMENTED | CLI format consistent |

**Issues:**
- ⚠️ `filter` command not documented in UserGuide despite being high priority

---

### 3.2 Medium Priority (Nice to Have) - `* *`

| User Story | Implementation Status | Notes |
|------------|----------------------|-------|
| Add custom tags to contacts | ✓ IMPLEMENTED | `t/TAG` parameter in add/edit |
| Mark contacts as favorites | ✗ NOT IMPLEMENTED | No favorite functionality found |

**Missing Feature:** Marking contacts as favorites is not implemented in codebase.

---

### 3.3 Low Priority (Unlikely to Have) - `*`

| User Story | Implementation Status | Notes |
|------------|----------------------|-------|
| Sort my contacts | ⚠️ PARTIALLY IMPLEMENTED | `sort` command exists but not documented |
| Backup contact information | ✗ NOT IMPLEMENTED | Mentioned as future feature (v2.0) |

---

## 4. Non-Functional Requirements Verification

### 4.1 Verifiable Through Code Inspection

| Requirement | Status | Notes |
|-------------|--------|-------|
| NFR-1: Work on mainstream OS with Java 17+ | ✓ LIKELY MET | JavaFX application, standard Java code |
| NFR-3: CLI faster than mouse for fast typers | ✓ MET | All operations via commands |
| NFR-9: No external connectivity required | ✓ MET | Local JSON storage, no network calls found |

### 4.2 Requires Performance Testing (Cannot Verify Through Code Inspection)

| Requirement | Status | Notes |
|-------------|--------|-------|
| NFR-2: Hold up to 1000 persons without sluggishness | ⚠️ UNTESTED | Requires performance benchmarking |
| NFR-4: Backup/restore within 5 seconds for 1000 contacts | ⚠️ UNTESTED | Requires performance benchmarking |
| NFR-5: Application startup within 10 seconds | ⚠️ UNTESTED | Requires performance benchmarking |
| NFR-6: Memory usage not exceed 300MB | ⚠️ UNTESTED | Requires memory profiling |
| NFR-7: CRUD operations within 200ms (0-30 contacts) | ⚠️ UNTESTED | Requires performance benchmarking |
| NFR-8: Search/filter within 300ms (1000 contacts) | ⚠️ UNTESTED | Requires performance benchmarking |

**Note:** These requirements require runtime testing with various dataset sizes, which is beyond the scope of code inspection.

---

## 5. Documentation Inconsistencies

### 5.1 UserGuide vs DeveloperGuide

| Feature | In DeveloperGuide | In UserGuide | Status |
|---------|-------------------|--------------|--------|
| `add` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `list` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `edit` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `find` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `delete` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `clear` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `help` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `exit` command | ✓ Yes | ✓ Yes | ✓ Consistent |
| `filter` command | ✓ Yes (UC04) | ✗ No | ✗ MISSING |
| `sort` command | ✓ Yes (User Story) | ✗ No | ✗ MISSING |
| Timezone field (`tz/`) | ✗ No | ✗ No | ✗ MISSING |

### 5.2 Command Summary Issues

**UserGuide Command Summary (Section at end):**
- Lists: Add, Clear, Delete, Edit, Find, List, Help
- Missing: `filter`, `sort`, `exit`

**Recommendations:**
1. Add `filter` command to UserGuide with full documentation
2. Add `sort` command to UserGuide (if fully implemented)
3. Add `exit` command to command summary table
4. Document timezone parameter for `add` and `edit` commands

---

## 6. Architecture and Design Verification

### 6.1 Architecture Components

**From DeveloperGuide Section:**

| Component | Expected | Found in Code | Status |
|-----------|----------|---------------|--------|
| Main | ✓ | ✓ | ✓ PRESENT |
| MainApp | ✓ | ✓ | ✓ PRESENT |
| UI | ✓ | ✓ | ✓ PRESENT |
| Logic | ✓ | ✓ | ✓ PRESENT |
| Model | ✓ | ✓ | ✓ PRESENT |
| Storage | ✓ | ✓ | ✓ PRESENT |
| Commons | ✓ | ✓ | ✓ PRESENT |

**No Issues Found** - Architecture matches DeveloperGuide description

### 6.2 Model Component

**Expected from DeveloperGuide:**
- `Person` objects with fields: name, phone, email, address, tags
- `UniquePersonList`
- `UserPref`

**Actual Implementation:**
- ✓ `Person` objects exist
- ✓ Additional field: `Timezone` (NOT documented)
- ✓ `UniquePersonList` exists
- ✓ `UserPref` exists

**Issue:** Model diagrams in DeveloperGuide do not show Timezone field

---

## 7. Manual Testing Instructions Verification

### 7.1 DeveloperGuide Manual Testing Section

**Documented Test Cases:**
1. Launch and shutdown ✓ (documented)
2. Deleting a person ✓ (documented with test cases)
3. Saving data ⚠️ (placeholder only)

**Missing Test Cases:**
- Adding a person
- Editing a person
- Finding persons
- Filtering by tags
- Sorting contacts
- Listing contacts
- Testing with timezone field
- Testing duplicate detection
- Testing tag validation
- Testing with maximum dataset (1000 persons)
- Testing performance requirements (NFR 2-8)

**Recommendation:**
Expand manual testing section to cover all commands and use cases.

---

## 8. Code Quality Observations

### 8.1 Incomplete Implementation Indicators

**TODO Comments Found:**
```java
// From Timezone.java (line 33)
//todo ck: check who is sending null Timezone

// From Timezone.java (line 61)
//todo ck: for now keep as raw TZ value

// From SortCommand.java (line 11)
// TODO Add sort as a param e.g. s/ (will need to edit clisyntax.java)
```

**Unused Code Found:**
```java
// From SortCommand.java
public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Sort command not implemented yet!";
// This constant is defined but never used
```

**Incomplete Methods:**
```java
// From Timezone.java (line 50-52)
public String getTzOffset(Timezone other) {
    return "NOT IMPLEMENTED";
}
```

**Impact:** These suggest areas of the codebase that may need completion or cleanup.

---

## 9. Summary of Issues

### 9.1 Critical Issues (Must Address)

1. **Timezone field not documented** - High priority user-facing feature with no documentation
2. **Filter command not in UserGuide** - High priority feature (use case UC04) missing from user documentation

### 9.2 Major Issues (Should Address)

3. **Sort command status unclear** - Implementation complete but inconsistent messaging
4. **UserGuide command summary incomplete** - Missing filter, sort, exit commands
5. **Model diagrams incomplete** - Missing Timezone field

### 9.3 Minor Issues (Nice to Fix)

6. **TODO comments in production code** - Suggests incomplete implementation
7. **Unused constants** - MESSAGE_NOT_IMPLEMENTED_YET never used
8. **Incomplete methods** - getTzOffset returns "NOT IMPLEMENTED"
9. **Manual testing section incomplete** - Only 2 test scenarios documented
10. **Favorites feature missing** - Medium priority user story not implemented

### 9.4 Informational (No Action Required)

11. **Undo/Redo feature** - Properly documented as proposed, not implemented
12. **Data archiving feature** - Properly documented as future (v2.0)
13. **NFR performance requirements** - Cannot verify without runtime testing

---

## 10. Recommendations

### 10.1 Immediate Actions Required

1. **Document Timezone Feature**
   - Add to DeveloperGuide Model section
   - Add to UserGuide add/edit command syntax
   - Update use case UC01
   - Add examples with timezone parameter
   - Update model class diagrams

2. **Document Filter Command**
   - Add full section in UserGuide features
   - Add to command summary table
   - Add usage examples
   - Document 10-tag limit

3. **Clarify Sort Command Status**
   - Remove MESSAGE_NOT_IMPLEMENTED_YET if working
   - Add to UserGuide if ready for users
   - OR remove from available commands if not ready
   - Resolve TODO comment

4. **Update Command Summary**
   - Add missing commands: filter, sort, exit
   - Ensure all implemented commands are listed

### 10.2 Recommended Improvements

5. **Complete Manual Testing Section**
   - Add test cases for all commands
   - Add test cases for all use cases
   - Add edge case testing
   - Add performance testing guidelines

6. **Code Cleanup**
   - Resolve TODO comments
   - Implement or remove getTzOffset method stub
   - Remove unused constants
   - Add clarifying comments

7. **Consider Implementing Missing Medium Priority Feature**
   - Favorites functionality from user stories

### 10.3 Long-term Considerations

8. **Performance Testing**
   - Create test suite for NFR 2-8
   - Document actual performance benchmarks
   - Update DeveloperGuide with results

9. **Complete Proposed Features**
   - Implement or remove undo/redo design
   - Design or remove data archiving placeholder

---

## 11. Conclusion

The CustomerRelationBook application has a solid foundation with most core features implemented and working. The main issues are **documentation gaps** rather than functional problems:

**Strengths:**
- Core CRUD operations fully functional
- Architecture matches design documentation
- Use cases mostly implemented correctly
- Good separation of concerns in code structure
- Comprehensive test suite exists

**Weaknesses:**
- Timezone feature completely undocumented despite being implemented
- Filter command missing from UserGuide despite being high priority
- Sort command status unclear
- Manual testing section incomplete
- Some medium priority user stories not implemented

**Overall Assessment:**
The product is **functionally sound but documentation-incomplete**. With proper documentation updates, particularly for the Timezone and Filter features, the product would align well with its DeveloperGuide requirements.

---

## Appendix A: Testing Methodology

This verification was conducted through:
1. **Code Inspection** - Examined all command classes, parsers, and model classes
2. **Documentation Review** - Analyzed DeveloperGuide and UserGuide
3. **Test Code Analysis** - Reviewed existing test files to understand expected behavior
4. **Cross-Reference** - Compared documented use cases against implementation
5. **Architecture Validation** - Verified component structure matches design

**Limitations:**
- No runtime testing performed
- No GUI interaction testing performed
- No performance benchmarking conducted
- No stress testing with large datasets
- No user acceptance testing

---

## Appendix B: Files Examined

### Source Code
- `src/main/java/seedu/address/logic/commands/*.java` (all command classes)
- `src/main/java/seedu/address/logic/parser/*.java` (all parser classes)
- `src/main/java/seedu/address/model/person/*.java` (model classes)
- `src/main/java/seedu/address/model/tag/*.java` (tag classes)

### Documentation
- `docs/DeveloperGuide.md`
- `docs/UserGuide.md`
- `README.md`

### Test Data
- `src/test/data/JsonSerializableAddressBookTest/typicalPersonsAddressBook.json`
- `src/test/java/seedu/address/logic/commands/*.java` (test classes)

---

**Report Generated:** 2025-10-26  
**Prepared By:** Automated Code Inspection System  
**Report Version:** 1.0
