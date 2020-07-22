package sls.meetup.itau.kafka.consumer.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import sls.meetup.itau.kafka.consumer.entity.Status;

@Repository
public interface StatusRepository extends CrudRepository<Status, Long> {

    Status findByName(String name);
}
