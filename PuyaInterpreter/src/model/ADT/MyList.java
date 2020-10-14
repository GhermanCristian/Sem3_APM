package model.ADT;

import java.util.LinkedList;
import java.util.ListIterator;

class MyList<TElem> implements ListInterface<TElem> {
	// I am using linkedlist instead of arraylist because we are using this structure for "Out" - a list of messages
	// the operation that is usually done with Out is adding an element at the end / getting the last element (hopefully)
	
	// I am not doing List<> l = new LinkedList<>() because I need some methods from LL which are not available to List (getFirst, getLast)
	private LinkedList<TElem> list;
	
	public MyList() {
		this.list = new LinkedList<TElem>();
	}

	@Override
	public void addLast(TElem newElem) {
		this.list.add(newElem);
	}

	@Override
	public TElem getFirst() {
		return this.list.getFirst();
	}

	@Override
	public TElem getLast() {
		return this.list.getLast();
	}

	@Override
	public boolean remove(TElem elem) {
		return this.list.remove(elem);
	}

	@Override
	public ListIterator<TElem> getIterator() {
		return this.list.listIterator();
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public void clear() {
		this.list.clear();
	}
}
