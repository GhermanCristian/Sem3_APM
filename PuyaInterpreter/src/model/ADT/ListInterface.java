package model.ADT;

import java.util.ListIterator;

interface ListInterface<TElem> {
	public void addLast(TElem newElem);
	public boolean remove(TElem elem);
	public ListIterator<TElem> getIterator();
	public String toString();
	public int size();
	public void clear();
}
