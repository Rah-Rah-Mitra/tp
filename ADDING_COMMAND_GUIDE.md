# Guide to Adding a Command to AddressBook

This guide documents the complete procedure for adding a new command to the AddressBook application, using the `remark` command as a reference example.

## Table of Contents
1. [Overview](#overview)
2. [Step-by-Step Implementation](#step-by-step-implementation)
3. [File Modification Checklist](#file-modification-checklist)
4. [Commit Strategy](#commit-strategy)
5. [Testing](#testing)

---

## Overview

When adding a new command that involves adding a field to a `Person`, you need to update multiple layers of the application:
- **Model Layer**: Create the field class and update Person
- **Logic Layer**: Create the command and parser
- **Storage Layer**: Update JSON serialization
- **UI Layer**: Display the new field
- **Test Layer**: Update test utilities and test cases

### Example: The `remark` Command

**Purpose**: Add optional remarks to persons in the address book

**Usage**: `remark INDEX r/REMARK`

**Example**: `remark 2 r/Likes baseball`

---

## Step-by-Step Implementation

### Step 1: Create the Model Class

**File**: `src/main/java/seedu/address/model/person/Remark.java`

Create a new class to represent the field. For fields with no validation constraints:

```java
package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's remark in the address book.
 * Guarantees: immutable; is always valid
 */
public class Remark {

    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A remark string.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        value = remark;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Remark)) {
            return false;
        }

        Remark otherRemark = (Remark) other;
        return value.equals(otherRemark.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```

**Commit**: `"Add Remark model class and PREFIX_REMARK to CliSyntax"`

---

### Step 2: Add Prefix to CliSyntax

**File**: `src/main/java/seedu/address/logic/parser/CliSyntax.java`

Add the prefix definition for your new field:

```java
public static final Prefix PREFIX_REMARK = new Prefix("r/");
```

**Commit**: Include with Step 1

---

### Step 3: Update Person Model

**File**: `src/main/java/seedu/address/model/person/Person.java`

1. **Add the field**:
```java
private final Remark remark;
```

2. **Update constructor**:
```java
public Person(Name name, Phone phone, Email email, Address address, Remark remark, Set<Tag> tags) {
    requireAllNonNull(name, phone, email, address, remark, tags);
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.address = address;
    this.remark = remark;
    this.tags.addAll(tags);
}
```

3. **Add getter**:
```java
public Remark getRemark() {
    return remark;
}
```

4. **Update equals() method**:
```java
return name.equals(otherPerson.name)
        && phone.equals(otherPerson.phone)
        && email.equals(otherPerson.email)
        && address.equals(otherPerson.address)
        && remark.equals(otherPerson.remark)
        && tags.equals(otherPerson.tags);
```

5. **Update hashCode() method**:
```java
return Objects.hash(name, phone, email, address, remark, tags);
```

6. **Update toString() method**:
```java
return new ToStringBuilder(this)
        .add("name", name)
        .add("phone", phone)
        .add("email", email)
        .add("address", address)
        .add("remark", remark)
        .add("tags", tags)
        .toString();
```

**Commit**: `"Update Person model to include Remark field"`

---

### Step 4: Create the Command Class

**File**: `src/main/java/seedu/address/logic/commands/RemarkCommand.java`

```java
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Changes the remark of an existing person in the address book.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "r/ [REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + "r/ Likes to swim.";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Person: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Person: %1$s";

    private final Index index;
    private final Remark remark;

    /**
     * @param index of the person in the filtered person list to edit the remark
     * @param remark of the person to be updated to
     */
    public RemarkCommand(Index index, Remark remark) {
        requireAllNonNull(index, remark);

        this.index = index;
        this.remark = remark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), remark, personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether
     * the remark is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !remark.value.isEmpty() ? MESSAGE_ADD_REMARK_SUCCESS : MESSAGE_DELETE_REMARK_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        RemarkCommand e = (RemarkCommand) other;
        return index.equals(e.index)
                && remark.equals(e.remark);
    }
}
```

**Commit**: `"Add RemarkCommand and RemarkCommandParser"`

---

### Step 5: Create the Parser Class

**File**: `src/main/java/seedu/address/logic/parser/RemarkCommandParser.java`

```java
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

/**
 * Parses input arguments and creates a new RemarkCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns a RemarkCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    RemarkCommand.MESSAGE_USAGE), ive);
        }

        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("");

        return new RemarkCommand(index, new Remark(remark));
    }
}
```

**Commit**: Include with Step 4

---

### Step 6: Hook Command into AddressBookParser

**File**: `src/main/java/seedu/address/logic/parser/AddressBookParser.java`

1. **Add import**:
```java
import seedu.address.logic.commands.RemarkCommand;
```

2. **Add case to switch statement**:
```java
case RemarkCommand.COMMAND_WORD:
    return new RemarkCommandParser().parse(arguments);
```

**Commit**: `"Hook RemarkCommand into AddressBookParser"`

---

### Step 7: Update Existing Commands

You need to update all places where `Person` objects are created:

#### AddCommandParser
**File**: `src/main/java/seedu/address/logic/parser/AddCommandParser.java`

```java
Person person = new Person(name, phone, email, address, new Remark(""), tagList);
```

#### EditCommand
**File**: `src/main/java/seedu/address/logic/commands/EditCommand.java`

In `createEditedPerson()`:
```java
return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress,
        personToEdit.getRemark(), updatedTags);
```

#### SampleDataUtil
**File**: `src/main/java/seedu/address/model/util/SampleDataUtil.java`

Add empty remark to all sample persons:
```java
new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
    new Address("Blk 30 Geylang Street 29, #06-40"), new Remark(""),
    getTagSet("friends"))
```

**Commit**: `"Update existing commands to support Remark field"`

---

### Step 8: Update Storage Layer

**File**: `src/main/java/seedu/address/storage/JsonAdaptedPerson.java`

1. **Add field**:
```java
private final String remark;
```

2. **Update constructor** (with @JsonCreator):
```java
public JsonAdaptedPerson(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
        @JsonProperty("email") String email, @JsonProperty("address") String address,
        @JsonProperty("remark") String remark,
        @JsonProperty("tags") List<JsonAdaptedTag> tags) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.address = address;
    this.remark = remark;
    if (tags != null) {
        this.tags.addAll(tags);
    }
}
```

3. **Update conversion constructor**:
```java
public JsonAdaptedPerson(Person source) {
    name = source.getName().fullName;
    phone = source.getPhone().value;
    email = source.getEmail().value;
    address = source.getAddress().value;
    remark = source.getRemark().value;
    tags.addAll(source.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList()));
}
```

4. **Update toModelType()**:
```java
final Remark modelRemark = new Remark(remark == null ? "" : remark);
return new Person(modelName, modelPhone, modelEmail, modelAddress, modelRemark, modelTags);
```

**Important**: Delete the old `data/addressbook.json` file before running to avoid deserialization errors!

**Commit**: `"Update JsonAdaptedPerson for Remark field"`

---

### Step 9: Update UI Components

#### PersonCard.java
**File**: `src/main/java/seedu/address/ui/PersonCard.java`

1. **Add field**:
```java
@FXML
private Label remark;
```

2. **Update constructor**:
```java
remark.setText(person.getRemark().value);
```

#### PersonListCard.fxml
**File**: `src/main/resources/view/PersonListCard.fxml`

Add the label element:
```xml
<Label fx:id="remark" styleClass="cell_small_label" text="\$remark" />
```

**Commit**: `"Add Remark display to UI"`

---

### Step 10: Update Test Utilities

#### PersonBuilder
**File**: `src/test/java/seedu/address/testutil/PersonBuilder.java`

1. **Add constant**:
```java
public static final String DEFAULT_REMARK = "";
```

2. **Add field**:
```java
private Remark remark;
```

3. **Initialize in constructor**:
```java
remark = new Remark(DEFAULT_REMARK);
```

4. **Update copy constructor**:
```java
remark = personToCopy.getRemark();
```

5. **Add builder method**:
```java
public PersonBuilder withRemark(String remark) {
    this.remark = new Remark(remark);
    return this;
}
```

6. **Update build()**:
```java
return new Person(name, phone, email, address, remark, tags);
```

#### CommandTestUtil
**File**: `src/test/java/seedu/address/logic/commands/CommandTestUtil.java`

Add test constants:
```java
public static final String VALID_REMARK_AMY = "Like skiing.";
public static final String VALID_REMARK_BOB = "Favourite pastime: Eating";
```

#### JsonAdaptedPersonTest
**File**: `src/test/java/seedu/address/storage/JsonAdaptedPersonTest.java`

1. **Add constant**:
```java
private static final String VALID_REMARK = BENSON.getRemark().toString();
```

2. **Update all test constructors** to include remark parameter:
```java
new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS)
```

**Note**: Watch out for line length! Keep lines under 120 characters per checkstyle rules.

**Commit**: `"Update test utilities for Remark support"`

---

## File Modification Checklist

Use this checklist when adding a new field to Person:

- [ ] **Model Layer**
  - [ ] Create new field class (e.g., `Remark.java`)
  - [ ] Add prefix to `CliSyntax.java`
  - [ ] Update `Person.java` (field, constructor, getter, equals, hashCode, toString)

- [ ] **Logic Layer**
  - [ ] Create command class (e.g., `RemarkCommand.java`)
  - [ ] Create parser class (e.g., `RemarkCommandParser.java`)
  - [ ] Update `AddressBookParser.java`
  - [ ] Update `AddCommandParser.java`
  - [ ] Update `EditCommand.java`
  - [ ] Update `SampleDataUtil.java`

- [ ] **Storage Layer**
  - [ ] Update `JsonAdaptedPerson.java` (field, constructors, toModelType)
  - [ ] Delete old `data/addressbook.json` file

- [ ] **UI Layer**
  - [ ] Update `PersonCard.java` (field, binding)
  - [ ] Update `PersonListCard.fxml` (add label)

- [ ] **Test Layer**
  - [ ] Update `PersonBuilder.java`
  - [ ] Update `CommandTestUtil.java`
  - [ ] Update `JsonAdaptedPersonTest.java`
  - [ ] Create command test class (e.g., `RemarkCommandTest.java`) [Optional but recommended]

---

## Commit Strategy

Organize commits logically by layer/functionality:

1. **Model + Syntax**: `"Add [Field] model class and PREFIX_[FIELD] to CliSyntax"`
2. **Person Update**: `"Update Person model to include [Field] field"`
3. **Command + Parser**: `"Add [Command]Command and [Command]CommandParser"`
4. **Parser Integration**: `"Hook [Command]Command into AddressBookParser"`
5. **Existing Commands**: `"Update existing commands to support [Field] field"`
6. **Storage**: `"Update JsonAdaptedPerson for [Field] field"`
7. **UI**: `"Add [Field] display to UI"`
8. **Tests**: `"Update test utilities for [Field] support"`

### Benefits of Batched Commits:
- **Easy to review**: Each commit focuses on one aspect
- **Easy to debug**: Can identify which layer caused issues
- **Easy to revert**: Can roll back specific changes
- **Clear history**: Shows logical progression of implementation

---

## Testing

### Manual Testing Steps:

1. **Build the project**:
   ```bash
   ./gradlew.bat build
   ```

2. **Delete old data** (Important!):
   ```bash
   rm data/addressbook.json  # or manually delete
   ```

3. **Run the application**:
   ```bash
   ./gradlew.bat run
   ```

4. **Test the command**:
   - Add a person: `add n/John Doe p/12345678 e/john@example.com a/123 Street`
   - Add remark: `remark 1 r/Likes basketball`
   - View the remark in the UI
   - Edit remark: `remark 1 r/Prefers football`
   - Clear remark: `remark 1 r/`

5. **Test persistence**:
   - Close application
   - Reopen application
   - Verify remarks are still present

### Running Tests:

```bash
./gradlew.bat test
```

### Common Issues:

1. **Deserialization error on startup**:
   - Solution: Delete `data/addressbook.json`

2. **Checkstyle violations**:
   - Solution: Ensure lines are < 120 characters
   - Check proper Javadoc formatting

3. **Tests failing**:
   - Ensure all `Person` constructor calls include the new field
   - Verify test data includes the new field

---

## Additional Resources

- [AB3 Tutorial: Adding a Command](https://se-education.org/addressbook-level3/tutorials/AddRemark.html)
- [Developer Guide](https://se-education.org/addressbook-level3/DeveloperGuide.html)
- [Testing Guide](https://se-education.org/addressbook-level3/Testing.html)

---

## Example: Creating Your Own Command

When creating a different command (not remark), follow the same structure:

1. Replace "Remark" with your field name (e.g., "Birthday", "Role", "Department")
2. Replace "remark" with your command word
3. Replace "r/" with your chosen prefix (e.g., "b/", "ro/", "d/")
4. Add appropriate validation if needed (unlike Remark, which has no constraints)
5. Follow the same commit batches
6. Update this guide with your specific implementation details

**Pro tip**: For fields with validation (like Email, Phone), study those classes to understand the validation pattern.

---

*Last Updated: October 1, 2025*
