## Notes on piro360

# Notes By Kris from Bugz2

_Once you generate a JHIPSTER project, how do you modify it to restrict certain entities related to a `User` onto a new page in the front-end?_

This is a common enough need in many ZipCode Spring projects.

## Adding a simple vanillaJS

look in `bugz2/code/src/main/resources/static` to find the three files that respond when you call the
`/tickets.html` url.
Very simple JS code to fetch a json API call and display the results simply. (See `app.js` in `static`)

## Generating the Code

JHIPSTER is good at generating a large amount of code on top of the Spring App engine.
It will produce a good starter project and allows you, with the help of the entity generating machinery, to customize it to your project's domain of objects.
After the initial generation of the project, you need to create a `jdl` file which contains descriptions of the data model you'd like your app to use.

### JDL - a useful addition for working at a high level

JHIPSTER's JDL machinery can generate large amount of useful backend REST API code in the generated Spring App server for a series of Entities (the objects that you model in your app).
This project **Bugzee**, it meant to be an example of a simple Issue Tracking app, where `Tickets` are used to track issues related to some `Project`.
`Tickets` can be `ReportedBy` and `AssignedTo` a given `User`.
(Look at the [jhipster-jdl-diagram](./jhipster-jdl-diagram.png) for a visual of the Entity-Relationship Diagram (a specialized UML)).

THis diagram is a visual of the [JDL file for Bugzee](./bugzee.jdl) which describes the `domain objects` or the `business objects` used by this Issue/Ticket/Bug Tracker project.

![jhipster-jdl-diagram](./jhipster-jdl-diagram.png)

### Adding a "restricted" view of Tickets

A common milestone early in a project, is to restrict some items from the db by showing only those items "owned" by a User.
If in the JDL file you have declared a relationship between one or more of you domain objects, JHIPSTER will generate some extra code in the entity JPA repositories files to allow you to restrict a query to just the objects that are "thru" that relationship.

```jdl
relationship ManyToOne {
  Ticket{project(name)} to Project
  Ticket{assignedTo(login)} to User
  Ticket{reportedBy(login)} to User
  Comment{login} to User
}
```

This declaration, among other things, generates a handy couple of methods in the `TicketRepository` class.

```java
@Repository
public interface TicketRepository extends TicketRepositoryWithBagRelationships, JpaRepository<Ticket, Long> {
    @Query("select ticket from Ticket ticket where ticket.assignedTo.login = ?#{principal.username}")
    List<Ticket> findByAssignedToIsCurrentUser();

    @Query("select ticket from Ticket ticket where ticket.reportedBy.login = ?#{principal.username}")
    List<Ticket> findByReportedByIsCurrentUser();

    /* a WHOLE lot more...*/
```

JHIPSTER will produce a CRUD interface in the front-end that allows you access to the entities you add to the project with your JDL file.
The problem is, those pages show _all_ the Tickets (in this case Tickets are the entity which track "bugs") to a user, and while one can sort via a column, it's less than ideal.
So, we need pages which will show a User the Tickets that have been `AssignedTo` them and to show the Tickets which the User has `ReportedBy`.
In this project, showing the User the Tickets they have been `AssignedTo` (and only those) is the first task in this.
The second is to mimic the first set of changes to show the user only the Tickets which have been `ReportedBy` them.

Modify the test data records to have a couple bugs which are assigned and reported by the `User` ADMIN and a few assigned and reported by `User` USER.

I did this twice.
The first one `listuser` was a copy of the `code/src/main/webapp/app/entities/ticket/list` component.
Yes, you could generate this component from scratch.
I chose to do it by copying a working set of files, and them modifying them so that I could leverage the "spidery" connections you find in these Angular/React frontends.
It turns out there are a bunch of files you have to edit to "wire" it all into the project.
It gives me a subset of `Tickets` restricted by the `AssignedTo` relationship.

The second one was the `listreport` set of changes, giving me a `ReportedBy` subset of `Tickets`.

### Adding a AssignedTo Tickets `listuser` (assignedto) page.

- copied `list/` folder of components to `listuser/`.
- change all `TicketComponent` to `TickerUserComponent`
- added the API call in the back end to `TicketResource` by findByAssignedToIsCurrentUser
- added to `TicketService` `queryassign(req?: any): Observable<EntityArrayResponseType>`
- added a link to the Home component
- modified `code/src/main/webapp/app/entities/ticket/ticket.module.ts` adding TicketUserComponent
- added to `code/src/main/webapp/app/entities/ticket/route/ticket-routing.module.ts` items for `assign` option.

### Adding a ReportedBy Tickets `listreport` page.

- copied `list/` folder of components to `listreported/`.
- change all `TicketComponent` to `TicketReportComponent`
- added the API call in the back end to `TicketResource` by findByReportedByIsCurrentUser
- added to `TicketService` `queryreport(req?: any): Observable<EntityArrayResponseType>`
- added a link to the Home component
- modified `code/src/main/webapp/app/entities/ticket/ticket.module.ts` adding TicketReportComponent
- added to `code/src/main/webapp/app/entities/ticket/route/ticket-routing.module.ts` items for `report` option.

These two activities leave the project with two added frontend URLs:

- `/ticket/assign` shows the `Tickets` that are `assignedTo` the current logged in User.
- `/ticket/report` shows the `Tickets` that are `reportedTo` the current logged in User.

I then placed these on the `Main Component` the "main page" of the app. See `code/src/main/webapp/app/home/home.component.html` (and notice how they are placed so that you only see them when you are logged in as a user.)
