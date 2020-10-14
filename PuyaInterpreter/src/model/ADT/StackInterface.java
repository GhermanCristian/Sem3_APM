package model.ADT;

interface StackInterface<TElem> {
	TElem pop();
	void push(TElem newElem);
	TElem top();
	int size();
	void clear();
}
