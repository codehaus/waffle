include_class 'org.codehaus.waffle.example.jruby.model.Person'

class PersonController

  def index
    @person_dao = locate(org.codehaus.waffle.example.jruby.dao.PersonDAO)

    @people = @person_dao.findAll
    render 'person.rhtml'
  end

  def remove(uid)
    @person_dao.delete(uid.to_i)

    return nil
  end

  def select(uid)
    @person = @person_dao.findById(uid.to_i)

    return nil
  end

  def save
    p "SAVING #{@person}"
    @person_dao.save(@person)

    return nil
  end

  def create
    @person = Person.new

    return nil # stay on page
  end

  def cancel
    @person = nil

    return nil
  end

end