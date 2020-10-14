package model.ADT;

import java.util.ListIterator;

interface ListInterface<TElem> {
	void addLast(TElem newElem);
	TElem getFirst();
	TElem getLast();
	boolean remove(TElem elem);
	ListIterator<TElem> getIterator();
	int size();
	void clear();
}
