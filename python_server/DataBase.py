
from typing import List, TypedDict
from Song import Song
from Author import Author

class Database(TypedDict):
    songs: List[Song]
    authors: List[Author]
