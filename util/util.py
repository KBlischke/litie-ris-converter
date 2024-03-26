"""
A Python module for processing entries from a file
and calculating statistics based on regular expressions.

This module contains functions to read entries from a file,
process user input, and calculate statistics based on the specified regular expressions.

Functions available:
- main(): The main function of the program, which calls other functions to read entries,
  process user input, and calculate statistics.
- get_user_input(): A function that prompts the user to input fields
  and corresponding regular expressions for searching.
- get_entries(file: str, encoding: str, delimiter: str) -> list:
  A function to read entries from a file.
- get_statistics(entries: list, fields: dict) -> dict:
  A function to calculate statistics based on regular expressions for each field in the entries.

Raises:
    FileNotFoundError: If the specified file is not found.

Returns:
    None
"""

import re
import sys


def main() -> None:
    """
    Main function of the program.

    This function calls other functions to read entries from a file,
    process user input, and calculate statistics based on regular expressions.
    It checks for each line of the entries if the custom fields match.
    Then, it outputs the statistics for each field.

    Raises:
        FileNotFoundError: If the specified file is not found.

    Returns:
        None
    """
    try:
        entries = get_entries("../data/litie34.dbm", "ansi", "\n\n")
    except FileNotFoundError as e:
        print(f"File not found: {e.filename}")
        sys.exit(1)

    fields = get_user_input()

    statistics = get_statistics(entries, fields)
    print("#" * 10)
    for field, statistic in statistics.items():
        try:
            searched = f'"{fields[field]}" in {field}' if fields[field] else f"{field}"
        except KeyError:
            searched = f"{field}"
        print(f"{searched}: {statistic}")


def get_user_input() -> dict:
    """
    Prompt the user to input fields and corresponding regular expressions for searching.

    Returns:
        dict: A dictionary mapping fields to their corresponding regular expressions.
    """
    fields = {}
    while True:
        field = input("Field to search: ").strip()
        if not field:
            break
        content = input("Regex in field to search: ").strip()
        fields[field] = content
    return fields


def get_entries(file: str, encoding: str, delimiter: str) -> list:
    """
    Reads entries from a file.

    Args:
        file (str): The name of the file to read entries from.
        encoding (str): The encoding of the file.
        delimiter (str): The delimiter used to separate entries in the file.

    Returns:
        list: A list of entries read from the file.
    """
    with open(file, "r", encoding=encoding) as f:
        entries = f.read().split(delimiter)
    return entries


def get_statistics(entries: list, fields: dict) -> dict:
    """
    Calculates statistics based on regular expressions for each field in the entries.

    Args:
        entries (list): A list of entries to process.
        fields (dict): A dictionary mapping fields to their corresponding regular expressions.

    Returns:
        dict: A dictionary containing the statistics for each field.
    """
    statistics = {field: 0 for field in fields}

    statistics["All"] = 0
    has_multiple_fields = len(fields) > 1

    patterns = {
        field: re.compile(rf"^\s*{field}\s*:(.+)$", re.MULTILINE) for field in fields
    }

    for entry in entries:
        has_all = True
        for field, value in fields.items():
            content_match = patterns[field].search(entry)

            if content_match:
                content = content_match.group(1)
                if value and re.search(rf"{value}", content):
                    statistics[field] += 1
                elif not value:
                    statistics[field] += 1
                else:
                    has_all = False
            else:
                has_all = False

        if has_all and has_multiple_fields:
            statistics["All"] += 1

    return statistics


if __name__ == "__main__":
    main()
