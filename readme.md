# Adders / Removers generator

This plugin allows to generate adders and removers methods for class properties based on annotated type.

Recognized types are: 

* `array` 
* `ClassName[]` 
* `Doctrine\Common\Collections\Collection`

   **Note:** Any specific collection implementation is also supported, e.g. `Doctrine\Common\Collections\ArrayCollection`.

It is possible to generate only adders, only removers or both at the same time: plugin adds three menu items to class "Generate" context menu

![context menu](https://i.imgur.com/ExPnHNN.png)


*__Works in PhpStorm 2022.2__*

## Examples

### Before

```php
namespace App\Entity;

class Entity
{
    /**
     * @var array
     */
    protected $users;
}
```
### After

```php
namespace App\Entity;

class Entity
{
    /**
     * @var array
     */
    protected $users;

    public function addUser(mixed $user): self
    {
        $this->users[] = $user;
        return $this;
    }

    public function removeUser(mixed $user): self
    {
        if ($key = array_search($user, $this->users, true) !== false) {
            array_splice($this->users, $key, 1);
        }
        return $this;
    }
}
```
---
### Before

```php
namespace App\Entity;

class Entity
{
    /**
     * @var User[]
     */
    protected $users;
}
```

### After

```php
namespace App\Entity;

class Entity
{
    /**
     * @var User[]
     */
    protected $users;

    public function addUser(User $user): self
    {
        $this->users[] = $user;
        return $this;
    }

    public function removeUser(User $user): self
    {
        if ($key = array_search($user, $this->users, true) !== false) {
            array_splice($this->users, $key, 1);
        }
        return $this;
    }
}
```
---
### Before

```php
namespace App\Entity;

use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;

class Entity
{
    /**
     * @var User[]|Collection // or ArrayCollection
     */
    protected $users;
}
```

### After

```php
namespace App\Entity;

use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;

class Entity
{
    /**
     * @var User[]|Collection // or ArrayCollection
     */
    protected $users;

    public function addUser(User $user): self
    {
        $this->users->add($user);
        return $this;
    }

    public function removeUser(User $user): self
    {
        $this->users->removeElement($user);
        return $this;
    }
}
```
---
### Before

```php
namespace App\Entity;

use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;

class Entity
{
    protected Collection $users;
}
```

### After

```php
namespace App\Entity;

use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

class Entity
{
    protected Collection $users;

    public function addUser(mixed $user): self
    {
        $this->users->add($user);
        return $this;
    }

    public function removeUser(mixed $user): self
    {
        $this->users->removeElement($user);
        return $this;
    }
}
```
---
### Before

```php
namespace App\Entity;

use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

class Entity
{
    #[ORM\OneToMany(
        mappedBy: 'user',
        targetEntity: Message::class,
        cascade: ['remove'],
        fetch: 'EXTRA_LAZY'
    )]
    protected Collection $users;
}
```

### After

```php
namespace App\Entity;

use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;

class Entity
{
    #[ORM\OneToMany(
        mappedBy: 'user',
        targetEntity: Message::class,
        cascade: ['remove'],
        fetch: 'EXTRA_LAZY'
    )]
    protected Collection $users;

    public function addUser(User $user): self
    {
        $this->users->add($user);
        return $this;
    }

    public function removeUser(User $user): self
    {
        $this->users->removeElement($user);
        return $this;
    }
}
```
---
## Edit templates
To edit templates used for generating methods go to:
`Settings` -> `Editor` -> `File and Code Templates` -> `Other` -> `Adder/Remover`

![edit templates](https://i.imgur.com/Ss4NOHD.png)
