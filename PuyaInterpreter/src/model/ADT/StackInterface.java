package model.ADT;

public interface StackInterface<TElem> {
	public int size();
	public void clear();
	public void push(TElem newElem);
	public TElem pop();
	public TElem top();
	public String toString();
}
