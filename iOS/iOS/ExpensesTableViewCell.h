//
//  ExpensesTableViewCell.h
//  accountably
//
//  Created by Steven Palomino on 1/14/15.
//  Copyright (c) 2015 Steven Palomino. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ExpensesTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *expenseTitle;
@property (weak, nonatomic) IBOutlet UILabel *expenseAmount;
@property (weak, nonatomic) IBOutlet UILabel *expensePriority;

@end
