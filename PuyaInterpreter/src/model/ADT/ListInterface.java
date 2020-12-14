package model.ADT;

import java.util.ListIterator;
import java.util.function.Consumer;

public interface ListInterface<TElem> {
	public void addLast(TElem newElem);
	public boolean remove(TElem elem);
	public TElem pop() throws Exception;
	public TElem get(int index) throws Exception;
	public ListIterator<TElem> getIterator();
	public void forEach(Consumer<? super TElem> action);
	public int size();
	public String toString();
	public void clear();
	public boolean isEmpty();
	public TElem getLast() throws Exception;
}
