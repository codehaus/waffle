include_class 'org.codehaus.waffle.example.jruby.model.Person'

class PersonController

  def index
    @person_dao = locate(org.codehaus.waffle.example.jruby.dao.PersonDAO)

    @persons = @person_dao.findAll
    render 'person.jspx'
  end

#
#  def remove(person_id)
#    @person_dao.delete(person_id)
#  end
#
  def select(uid)
    p "XXXXXXXXX #{uid}"
    @person = @person_dao.findById(uid.to_i)

    return nil
  end


  def save
    @person_dao.save(@person)

    return nil
  end

  def create
    @person = Person.new

    return nil # stay on page
  end
  
#
#  def cancel
#    @person = null
#  end

end