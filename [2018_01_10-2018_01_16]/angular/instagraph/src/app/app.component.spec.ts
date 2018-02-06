import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';

describe('AppComponent', () => {
  
  // preparation de l'environnement avant test
  // mise en place du "faux" environement navigateur/angular
  beforeEach(async(() => {
    // testBed est notre "support" de test
    TestBed.configureTestingModule({
      declarations: [
        AppComponent  // nous testons un composant, on met en place sa "declaration"
      ],
    }).compileComponents();
  }));

  // chaque fonction it déclare un test individuel
  // it ("ce qui est testé", async(() => { code de test }))
  it('should create the app', async(() => {
    // on demande a creer le composant a tester
    // cela nous renvoie une fixture qui contiendra les objets/elements a tester
    const fixture = TestBed.createComponent(AppComponent);

    // on peut récupérer l'instance du composant avec  'fixture.debugElement.componentInstance'
    const app = fixture.debugElement.componentInstance;
    
    // ce test tres basique verifie que le composnat a bien put etre creer
    expect(app).toBeTruthy();

  }));

  // un deuxieme test un peu plus intéréssant
  it(`should have as title 'app'`, async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    // on verifie que l'attribut title de mon instance AppComponent contient bien la chaine 'app'
    expect(app.title).toEqual('app');
  }));
  it('should render title in a h1 tag', async(() => {

    const fixture = TestBed.createComponent(AppComponent);
    // le fonction detectchange permet de "forcer" un rafraichissement d'angular
    fixture.detectChanges();
    // recuperation le nativeElement -> le template html associé au composant
    const compiled = fixture.debugElement.nativeElement;
    // je peut requetter à l'interieur le contenu des balises ou attributs
    // avec queriSelector
    expect(compiled.querySelector('h1').textContent).toContain('Welcome to app!');
  }));
});
