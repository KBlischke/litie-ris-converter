"""
A Python module for testing the utility functions.

This module contains test functions to verify the functionality of the util module.

Functions available:

- main(): The main function to run all the test functions.
- test_get_statistics_fields(): A function to test the get_statistics function
  with various entries and fields.
- test_get_statistics_contents(): A function to test the
  get_statistics function with various entries and contents.

Returns:
    None
"""
import util


def main() -> None:
    """
    Main function to run all the test functions.

    Returns:
        None
    """
    test_get_statistics_fields()
    test_get_statistics_contents()


def test_get_statistics_fields() -> None:
    """
    Test function to verify the get_statistics function with different entries and fields.

    Returns:
        None
    """
    entries1 = [
        "000:00001\n030:m\n040:d\n050:2024\n&&&",
        "000:00002\n030:m\n040:d\n050:2024\n&&&",
        "000:00003\n030:m\n040:d\n050:2024\n&&&",
    ]
    fields1 = {"000": "", "030": "", "040": "", "050": ""}
    assert util.get_statistics(entries1, fields1) == {
        "000": 3,
        "030": 3,
        "040": 3,
        "050": 3,
        "All": 3,
    }

    entries2 = [
        "000:00001\n030:m\n&&&",
        "000:00002\n030:m\n040:d\n&&&",
        "000:00003\n030:m\n040:d\n050:2024\n&&&",
    ]
    fields2 = {"000": "", "030": "", "040": "", "050": ""}
    assert util.get_statistics(entries2, fields2) == {
        "000": 3,
        "030": 3,
        "040": 2,
        "050": 1,
        "All": 1,
    }


def test_get_statistics_contents() -> None:
    """
    Test function to verify the get_statistics function with different entries and contents.

    Returns:
        None
    """
    entries1 = [
        "000:00001\n030:m\n040:d\n050:2024\n&&&",
        "000:00002\n030:m\n040:d\n050:2022\n&&&",
        "000:00003\n030:m\n040:d\n050:2024\n&&&",
    ]
    fields1 = {"000": "", "030": "", "040": "", "050": "2024"}
    assert util.get_statistics(entries1, fields1) == {
        "000": 3,
        "030": 3,
        "040": 3,
        "050": 2,
        "All": 2,
    }

    entries2 = [
        "000:00001\n030:m\n040:e\n050:2024\n&&&",
        "000:00002\n030:m\n040:d\n050:2022\n&&&",
        "000:00003\n030:m\n040:d\n050:2024\n&&&",
    ]
    fields2 = {"000": "", "030": "", "040": "e", "050": "2024"}
    assert util.get_statistics(entries2, fields2) == {
        "000": 3,
        "030": 3,
        "040": 1,
        "050": 2,
        "All": 1,
    }


if __name__ == "__main__":
    main()
