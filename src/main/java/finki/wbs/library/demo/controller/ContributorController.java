package finki.wbs.library.demo.controller;

import finki.wbs.library.demo.model.Contributor;
import finki.wbs.library.demo.service.ContributorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/contributors")
public class ContributorController {

    private final ContributorService contributorService;

    public ContributorController(ContributorService contributorService) {
        this.contributorService = contributorService;
    }

    @GetMapping
    public List<Contributor> getContributors(){
        return this.contributorService.getContributors();
    }

    @GetMapping("/{name}")
    public Contributor getOneContributor(@PathVariable("name") String name){
            return this.contributorService.getOneContributor(name);

    }
}
