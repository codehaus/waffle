require_gem 'rubyzip'
require 'zip/zipfilesystem'
require 'zip/zip'

require 'find'
require 'rake/packagetask'

ROOT_DIR = pwd

mkdir_p('target')

desc "run maven"
task :maven do
    print "Starting maven...\n"
    #print `mvn clean package`
end

task :copy_archives => [:maven] do
    cp_r Dir.glob('framework/target/*.jar'), 'target'
    cp_r Dir.glob('simple-example/target/*.war'), 'target'
    cp_r Dir.glob('paranamer-example/target/*.war'), 'target'
end

task :copy_site do
    FileUtils.cp_r 'framework/target/site/', 'target'
end

task :copy_source do
    targetdir = File.join(pwd, mkdir_p('target/src'))
    
    print "Copying framework source code to #{targetdir}...\n"    
     
    rootdir = pwd
    cd 'framework/src/main/java', :verbose => false
    Find.find(".") { |path| `cp -p --parents #{path} #{targetdir}` if path =~ /\.java$/ }
    cd rootdir, :verbose => false
end


desc "Create a source zip file"
file "target/waffle.zip" do
  cd 'framework/src/main/java', :verbose => false

  copyAll = lambda do |zipfile|
    FileList['**/*.*'].each do |file|
      zipfile.add("source/#{file}", File.new(file).path)
    end
  end

  # prefix file name with '.' to prevent it from zipping itself 
  Zip::ZipFile.open(".waffle.zip", Zip::ZipFile::CREATE, &copyAll)
  mv '.waffle.zip', '../resources'
  cd "../resources"
  Zip::ZipFile.open(".waffle.zip", &copyAll)

  cd ROOT_DIR
  mv 'framework/src/main/resources/.waffle.zip', 'target/waffle.zip'
end

task :stage => [:maven, :copy_source, :copy_archives, :copy_site]

task :default => ["target/waffle.zip"]

Rake::PackageTask.new("waffle-full", "0.7-BETA-1") do |p|
    p.need_zip = true
    p.package_files.include('target')
    Find.find('target') { |path| p.package_files.include(path) }
    
    # todo can't we just copy files directly (jars and wars)?
end
