package finki.wbs.library.demo.service;

import finki.wbs.library.demo.DemoApplication;
import finki.wbs.library.demo.model.Contributor;
import finki.wbs.library.demo.model.exceptions.InvalidContributorNameException;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class ContributorService {

    private final List<Contributor> contributors= DemoApplication.contributors;

    public List<Contributor> getContributors(){
        return this.contributors;
    }

    public Contributor getOneContributor(String name){
        return this.contributors.stream().filter(c->c.getName().equals(name)).findFirst().orElseThrow(InvalidContributorNameException::new);
    }
}
