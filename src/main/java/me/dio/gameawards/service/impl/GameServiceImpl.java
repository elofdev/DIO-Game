package me.dio.gameawards.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import me.dio.gameawards.domain.model.Game;
import me.dio.gameawards.domain.model.GameRepositoty;
import me.dio.gameawards.service.GameService;
import me.dio.gameawards.service.exception.BusinessException;
import me.dio.gameawards.service.exception.NoContentException;
import net.bytebuddy.implementation.bytecode.Throw;

@Service
public class GameServiceImpl implements GameService {

	@Autowired
	private GameRepositoty gameRepository;	
	
	@Override
	public List<Game> findAll() {
		List<Game> games = gameRepository.findAll(Sort.by(Direction.DESC,"votes"));
		return games;
	}

	@Override
	public Game findById(Long Id) {
		Optional<Game> game = gameRepository.findById(Id);
		return game.orElseThrow(()-> new NoContentException());
	}

	@Override
	public void insert(Game game) {
		if(Objects.nonNull(game.getId())){
			throw new BusinessException("O ID é já existe!");
		} else {			
			gameRepository.save(game);
		}
		
	}

	@Override
	public void update(Long id, Game game) {
		Game gameDb = findById(id);
		if(gameDb.getId().equals(game.getId())) {
			gameRepository.save(game);
		}else {
			throw new BusinessException("Os IDs para alteração são divergentes!"); 
		}
		
	}

	@Override
	public void delete(Long id) {
		Game gameDb = findById(id);
		gameRepository.delete(gameDb);
		
	}

	@Override
	public void vote(Long id) {
		Game gameDb = findById(id);
		gameDb.setVotes(gameDb.getVotes() + 1);
		
		update(id, gameDb);
	}

}
