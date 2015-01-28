//
//  ViewController.m
//  accountably
//
//  Created by Steven Palomino on 1/12/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import "ViewController.h"
#import "ExpensesTableViewController.h"

@interface ViewController ()

@end

@implementation ViewController

-(void)viewWillAppear:(BOOL)animated
{
    NSLog(@"will appear");
    [[self navigationController]setNavigationBarHidden:YES];
    BOOL isLoggedIn = [[NSUserDefaults standardUserDefaults]boolForKey:@"isLoggedIn"];
    if (isLoggedIn) {
        ExpensesTableViewController *membersAreaVC = [self.storyboard instantiateViewControllerWithIdentifier:@"members"];
        UINavigationController *nav = [[UINavigationController alloc]initWithRootViewController:membersAreaVC];
        [self presentViewController:nav animated:NO
                         completion:nil];
    }

}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    // Do any additional setup after loading the view, typically from a nib.
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
