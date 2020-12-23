package model.ADT;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

import exception.EmptyADTException;
import exception.InvalidPositionException;

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
	public void forEach(Consumer<? super TElem> action) {
		for(TElem crtElem : this.list) {
			action.accept(crtElem);
		}
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

	@Override
	public TElem pop() throws Exception{
		int size = this.list.size();
		if (size == 0) {
			throw new EmptyADTException("MyList: Empty list");
		}
		return this.list.remove(size - 1);
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public TElem getLast() throws Exception {
		int size = this.list.size();
		if (size == 0) {
			throw new EmptyADTException("MyList: Empty list");
		}
		return this.list.get(size - 1);
	}

	@Override
	public TElem get(int index) throws Exception {
		if (index < 0 || index >= this.list.size()) {
			throw new InvalidPositionException("MyList: invalid position");
		}
		return this.list.get(index);
	}
}
