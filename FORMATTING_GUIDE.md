# Code Formatting Configuration Guide

This document explains the automatic formatting setup for this project to ensure code quality and consistency.

## 🎯 What Was Configured

Two configuration files were added to automatically handle formatting issues:

### 1. `.vscode/settings.json`
VS Code workspace settings that apply to all developers using VS Code.

### 2. `.editorconfig`
Cross-editor configuration that works with VS Code, IntelliJ IDEA, and other editors.

## ⚙️ Automatic Formatting Features

### For All Files
- ✅ **Trailing Whitespace Removal**: Automatically removes spaces at end of lines
- ✅ **Final Newline**: Adds newline at end of file (required by git/checkstyle)
- ✅ **Consistent Line Endings**: Uses LF for most files, CRLF for Windows batch files
- ✅ **UTF-8 Encoding**: Ensures consistent character encoding

### Language-Specific Settings

#### Java Files
- **Tab Size**: 4 spaces
- **Format on Save**: Enabled
- **Max Line Length**: 120 characters (checkstyle requirement)
- **Checkstyle Integration**: Automatically validates against project rules

#### Markdown Files (.md)
- **Trailing Whitespace**: Auto-removed on save
- **Word Wrap**: Enabled for better readability
- **Final Newline**: Auto-added

#### JSON/JSONC Files
- **Tab Size**: 4 spaces
- **Trailing Whitespace**: Auto-removed
- **Final Newline**: Auto-added

## 🚀 How to Use

### Automatic (Recommended)
Just save your files! The settings will automatically:
1. Remove trailing whitespace
2. Add final newline
3. Format code (for Java files)

### Manual Formatting
- **Format Document**: `Shift + Alt + F` (Windows/Linux) or `Shift + Option + F` (Mac)
- **Format Selection**: `Ctrl + K, Ctrl + F` (Windows/Linux) or `Cmd + K, Cmd + F` (Mac)

## 📋 VS Code Extensions (Recommended)

To get the most out of these settings, install:

1. **EditorConfig for VS Code**
   - Publisher: EditorConfig
   - ID: `EditorConfig.EditorConfig`
   - Purpose: Reads .editorconfig settings

2. **Checkstyle for Java**
   - Publisher: Sheng Chen
   - ID: `shengchen.vscode-checkstyle`
   - Purpose: Real-time checkstyle validation

3. **Language Support for Java(TM) by Red Hat**
   - Publisher: Red Hat
   - ID: `redhat.java`
   - Purpose: Java language support and formatting

Install with:
```bash
code --install-extension EditorConfig.EditorConfig
code --install-extension shengchen.vscode-checkstyle
code --install-extension redhat.java
```

## 🔧 Configuration Details

### `.vscode/settings.json` Key Settings

```json
{
    "files.trimTrailingWhitespace": true,      // Remove trailing spaces
    "files.insertFinalNewline": true,          // Add newline at EOF
    "files.trimFinalNewlines": true,           // Keep only one final newline
    "java.format.settings.url": "config/checkstyle/checkstyle.xml",
    "java.checkstyle.configuration": "${workspaceFolder}/config/checkstyle/checkstyle.xml"
}
```

### `.editorconfig` Key Settings

```ini
[*]
charset = utf-8
end_of_line = lf
insert_final_newline = true
trim_trailing_whitespace = true

[*.java]
indent_size = 4
max_line_length = 120
```

## ✅ Fixes Applied

The configuration fixes these common checkstyle issues:

### ✅ Fixed: Missing Final Newline
**Before**: `ERROR: no newline at EOF`
**After**: Automatically added on save

### ✅ Fixed: Trailing Whitespace
**Before**: `WARN: trailing whitespace`
**After**: Automatically removed on save

### ✅ Fixed: Inconsistent Indentation
**Before**: Mixed tabs and spaces
**After**: Consistent 4-space indentation

### ✅ Fixed: Line Endings
**Before**: Mixed CRLF and LF
**After**: LF for source files, CRLF for Windows batch files

## 🧪 Testing the Configuration

### Test 1: Trailing Whitespace
1. Add spaces at end of a line
2. Save the file
3. Spaces should be automatically removed

### Test 2: Final Newline
1. Remove the last line in a file
2. Save the file
3. Newline should be automatically added

### Test 3: Java Formatting
1. Mess up indentation in a Java file
2. Save the file (if `formatOnSave` is enabled)
3. Code should be auto-formatted

## 🔍 Checking for Issues

Before pushing, you can manually check for formatting issues:

### Check All Files
```bash
# On Linux/Mac (if available)
./run-checks.sh

# Manual checks
git diff --check
```

### Check Specific Files
```bash
# Check for trailing whitespace
grep -n ' $' filename.md

# Check for final newline (should output nothing)
tail -c 1 filename.txt | od -An -tx1
```

## 📝 Best Practices

1. **Save Frequently**: Let the auto-formatting work for you
2. **Review Diffs**: Check git diffs before committing
3. **Install Extensions**: Get the recommended VS Code extensions
4. **Update Config**: Adjust `.vscode/settings.json` for team preferences
5. **Share Config**: Commit both `.vscode/settings.json` and `.editorconfig`

## 🆘 Troubleshooting

### Issue: Settings Not Working

**Solution 1**: Restart VS Code
```
Ctrl+Shift+P → "Reload Window"
```

**Solution 2**: Check Extension Installation
```
Ctrl+Shift+X → Search "EditorConfig"
```

**Solution 3**: Check Settings
```
File → Preferences → Settings → Search "trim trailing"
```

### Issue: Java Formatting Not Working

**Solution**: Ensure Java extensions are installed
```bash
code --install-extension redhat.java
code --install-extension vscjava.vscode-java-pack
```

### Issue: Git Still Shows Whitespace Warnings

**Solution**: Configure Git to handle line endings
```bash
git config --global core.autocrlf input  # On Linux/Mac
git config --global core.autocrlf true   # On Windows
```

## 📊 Configuration Benefits

- ✅ **Automatic**: No manual formatting needed
- ✅ **Consistent**: Same style across all developers
- ✅ **Compliant**: Meets checkstyle requirements
- ✅ **Time-Saving**: No more fixing whitespace manually
- ✅ **Cross-Editor**: Works with VS Code, IntelliJ, etc.

## 🔄 Updating Configuration

If you need to modify the configuration:

1. **For VS Code Only**: Edit `.vscode/settings.json`
2. **For All Editors**: Edit `.editorconfig`
3. **Test Changes**: Verify with `./run-checks.sh`
4. **Commit**: Add updated config files to git

## 📚 Additional Resources

- [EditorConfig Documentation](https://editorconfig.org/)
- [VS Code User Settings](https://code.visualstudio.com/docs/getstarted/settings)
- [Java Checkstyle](https://checkstyle.org/)
- [Git Line Endings](https://git-scm.com/book/en/v2/Customizing-Git-Git-Configuration)

---

**Configuration Added**: October 2, 2025
**Project**: AddressBook (CS2103)
**Branch**: tutorial-adding-command

---

*With these settings, you should never have formatting issues again! 🎉*
