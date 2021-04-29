#ifndef GatorCrossing
#define GatorCrossing

int const enemyNum = 25; //Represents the # of obstacles/enemies on each lane
int speeds[5]; //Represents the array of randomly generated speeds

enum Dir { STOP = 0, LEFT = 1, RIGHT = 2, UP = 3, DOWN = 4 };

struct Position
{
	int x;
	int y;
};

class Gator
{
private:
	Position originalPivotPos; Position pivotPos;
	Dir direction;

public:
	Position bodyPos[3][3];
	//Determines the player's current position
	inline void CalcBodyPos();

	//Constructor
	Gator(int posX, int posY);
	//Resets the Gator to the default location
	void Reset();
	//Increments the Gator's position based on user key input
	void Move();

	//Getter method for the player's y-pivot position
	inline int getPivotPosY();
	//Setter method for the player's direction
	inline void setDirection(Dir dir);
};


//Enemy or Blank Space Class
class Obstacle
{
private:
	int speed; char bodyChar;
	Position originalPivotPos; Position pivotPos;
	Dir direction;
	int level;
public:
	Position bodyPos[3][3];
	//Determines enemies/blankspaces current position
	inline void CalcBodyPos();

	//Constructs the "Obstacle" object
	Obstacle(int posX, int posY, Dir dir, int spd, char ch);

	//Resets the enemy to the original/default position
	void Reset();

	//Changes the position of the "Obstacle"
	void Move();

	//Getter method for bodyChar
	inline char getBodyChar();
};

//The GameManager Class which handles the creation and maintenance of all objects in the game
class GameManager
{
private:
	int score; //Current score
	int level; //Current level
	int width; int height; //Width and height of the game screen

	//Booleans that are changed throughout the game's runtime and checked by Run()
	bool gameOver; bool paused; bool playing;


	Gator *player; //Pointer to a Gator object
	Obstacle *enemy[5][enemyNum]; //2D array of pointers to Obstacle objects, represents the set of enemies in one level								

public:

	//The constructor for the GameManager Class
	//Takes in w - the desired width, and h - the desired height of the screen
	GameManager(int h, int w);

	//Destructor for the GameManager
	~GameManager();

	//Resets the player and enemy positions to generate the next level
	void Reset();

	//Checks the user input for the game controls as well as the pause menu
	void CheckInput();

	//Increases and displays the score, then calls Reset() and increments the array speeds
	void scoreUp();

	//Handles the game logic
	void Logic();

	//Draws game components to the screen
	void Draw();

	//Creates the Main Menu for the game and checks input for the menu options
	void MainMenu();

	//References booleans to determine when to loop the game logic/draw functions or when to call GameOver()
	void Run();

	//Displays "Game Over" and resets the game to the main menu
	void GameOver();

	//Pauses the game and asks the player if they would like to resume
	//Takes in input to check if the player would like to resume
	void Pause();
	
};




#endif