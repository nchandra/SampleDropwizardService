package com.nishant.example.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.nishant.example.entities.Podcast;

public class PodcastDao {

	@PersistenceContext(unitName="demoRestPersistence")
	private EntityManager entityManager;

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Podcast> getPodcasts() {

		String qlString = "SELECT p FROM Podcast p";
		TypedQuery<Podcast> query = entityManager.createQuery(qlString, Podcast.class);

		return query.getResultList();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public List<Podcast> getRecentPodcasts(int numberOfDaysToLookBack) {

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(TimeZone.getTimeZone("UTC+1"));//Munich time
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, -numberOfDaysToLookBack);//substract the number of days to look back
		Date dateToLookBackAfter = calendar.getTime();

		String qlString = "SELECT p FROM Podcast p where p.insertionDate > :dateToLookBackAfter";
		TypedQuery<Podcast> query = entityManager.createQuery(qlString, Podcast.class);
		query.setParameter("dateToLookBackAfter", dateToLookBackAfter, TemporalType.DATE);

		return query.getResultList();
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public Podcast getPodcastById(Long id) {

		try {
			String qlString = "SELECT p FROM Podcast p WHERE p.id = ?1";
			TypedQuery<Podcast> query = entityManager.createQuery(qlString, Podcast.class);
			query.setParameter(1, id);

			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public Long deletePodcastById(Long id) {

		Podcast podcast = entityManager.find(Podcast.class, id);
		entityManager.remove(podcast);

		return 1L;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public Long createPodcast(Podcast podcast) {

		podcast.setInsertionDate(new Date());
		entityManager.persist(podcast);
		entityManager.flush();//force insert to receive the id of the podcast

		return podcast.getId();
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public int updatePodcast(Podcast podcast) {

		entityManager.merge(podcast);

		return 1;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
	public void deletePodcasts() {
		Query query = entityManager.createNativeQuery("TRUNCATE TABLE podcasts");
		query.executeUpdate();
	}

}
