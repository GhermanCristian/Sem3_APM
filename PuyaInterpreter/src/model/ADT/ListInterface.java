package model.ADT;

import java.util.ListIterator;

public interface ListInterface<TElem> {
	public void addLast(TElem newElem);
	public boolean remove(TElem elem);
	public TElem pop() throws Exception;
	public ListIterator<TElem> getIterator();
	public int size();
	public void clear();
	public boolean isEmpty();
	public TElem getLast() throws Exception;
}
