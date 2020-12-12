package model.ADT;

import java.util.function.Consumer;

public interface StackInterface<TElem> {
	public int size();
	public void clear();
	public void push(TElem newElem);
	public void forEach(Consumer<? super TElem> action);
	public TElem pop();
	public TElem top();
	public String toString();
}
