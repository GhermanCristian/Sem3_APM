package model.ADT;

interface StackInterface<TElem> {
	public int size();
	public void clear();
	public void push(TElem newElem);
	public TElem pop();
	public TElem top();
}
