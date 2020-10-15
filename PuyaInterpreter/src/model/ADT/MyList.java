package model.ADT;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MyList<TElem> implements ListInterface<TElem> {	
	private List<TElem> list;
	
	public MyList() {
		this.list = new ArrayList<TElem>();
	}

	@Override
	public void addLast(TElem newElem) {
		this.list.add(newElem);
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
	public String toString() {
		String representation = "";
		for(TElem elem : this.list) {
			representation += (elem.toString() + "\n");
		}
		return representation;
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
