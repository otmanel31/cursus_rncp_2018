package com.loncoto.secondJunitSpring.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.loncoto.secondJunitSpring.metier.Gazouille;

public class GazouilleDaoMock implements GazouilleDao {

	private List<Gazouille> gazouilles;
	
	public GazouilleDaoMock() {
	}

	public void addGazouilles(Gazouille ... gazouilles) {
		//System.out.println("ajout gazouilles");
		this.gazouilles = new ArrayList<>(Arrays.asList(gazouilles));
	}
	
	@Override
	public List<Gazouille> findAll() {
		return this.gazouilles;
	}

	@Override
	public Gazouille findById(final int id) {
		return this.gazouilles.stream().filter(g -> g.getId() == id)
										.findFirst()
										.orElse(null);
	}

	@Override
	public int save(Gazouille gazouille) {
		this.gazouilles.add(gazouille);
		gazouille.setId(this.gazouilles.stream()
									   .mapToInt(g -> g.getId())
									   .max()
									   .orElse(0) + 1);
		return 1;
	}

}
