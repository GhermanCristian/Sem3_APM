package model.ADT;

import java.util.ListIterator;

public interface ListInterface<TElem> {
	public void addLast(TElem newElem);
	public boolean remove(TElem elem);
	public ListIterator<TElem> getIterator();
	public int size();
	public void clear();
}
